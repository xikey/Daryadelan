package daryadelan.sandogh.zikey.com.daryadelan;

import android.Manifest;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.Payroll;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PayrollWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPayroll;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.PayrollServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.UserServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.NumberSeperator;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;

public class PayrollFooterActivity extends AppCompatActivity {

    private static final String KEY_YEAR = "YEAR";
    private static final String KEY_MONTH = "MONTH";
    private static final String KEY_PERSONEL_CODE = "PERSONEL_CODE";
    public final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STOTAGE = 2;
    private long year;
    private long month;

    private RecyclerView rvItem;
    private ItemAdapter adapter;

    private LinearLayout lyProgress;


    private IPayroll repo;

    private AppBarLayout AppBarLayout;

    private Payroll payroll;
    private long personelCode;

    private LinearLayout createPdf;
    private CustomPrintLayout customPrintLayout;
    private IUser userRepo;

    private TextView txtName;
    private TextView txtPersonelCode;
    private TextView txtTotalPrice;

    private User payrollUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll_footer);

        parseIntent();
        initToolbar();
        initRepo();
        getUserExtraInfos();
        initViews();
        initRecycleView();
        getData();
        initClickListeners();

        requestGetMessagePermission();


    }

    private void initClickListeners() {
        if (createPdf != null)
            createPdf.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        printLayout();
                    }


                }
            });
    }

    private void initRepo() {
        if (repo == null)
            repo = new PayrollServerRepo();

        if (userRepo == null)
            userRepo = new UserServerRepo();
    }

    private void initViews() {

        rvItem = (RecyclerView) findViewById(R.id.rtItem);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        AppBarLayout = (android.support.design.widget.AppBarLayout) findViewById(R.id.appbar);
        AppBarLayout.setExpanded(true);
        createPdf = (LinearLayout) findViewById(R.id.createPdf);
        customPrintLayout = (CustomPrintLayout) findViewById(R.id.customPrintLayout);

        txtName = (TextView) findViewById(R.id.txtName);
        txtPersonelCode = (TextView) findViewById(R.id.txtPersonelCode);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);

        try {
            FontChanger.applyYekanFont(findViewById(R.id.lyHeader));
            FontChanger.applyMainFont(findViewById(R.id.lyCollapsingView));
            FontChanger.applyMainFont(findViewById(R.id.customPrintLayout));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);
    }

    private void getData() {

        if (repo == null)
            return;
        lyProgress.setVisibility(View.VISIBLE);

        repo.getPayroll(getApplicationContext(), year, month, personelCode, new IRepoCallBack<PayrollWrapper>() {
            @Override
            public void onAnswer(PayrollWrapper answer) {
                lyProgress.setVisibility(View.GONE);
                if (answer == null) {
                    showError(getString(R.string.error_getting_data_please_try_again));
                    return;
                }

                if (answer.getData() == null || answer.getData().size() == 0) {
                    showError("فیش حقوق جهت نمایش وجود ندارد");
                    return;
                }

                try{
                    payrollUser=answer.getUser();
                    txtName.setText(answer.getUser().getFullName());
                    txtPersonelCode.setText(""+personelCode);

                }catch (Exception e){

                }

                MappingDate(answer.getData());
                createCustomLayput();
            }

            @Override
            public void onError(Throwable error) {

                lyProgress.setVisibility(View.GONE);
                showError(error.getMessage());
                return;
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });

    }

    private void initToolbar() {
        String title = "فیش حقوقی:  " + payroll.getMonthAsString() + " " + year;
        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, title, null);

    }

    private void parseIntent() {

        Intent data = getIntent();

        if (data == null)
            return;

        if (data.hasExtra(KEY_MONTH))
            month = data.getLongExtra(KEY_MONTH, 0);

        if (data.hasExtra(KEY_YEAR))
            year = data.getLongExtra(KEY_YEAR, 0);

        if (data.hasExtra(KEY_PERSONEL_CODE))
            personelCode = data.getLongExtra(KEY_PERSONEL_CODE, 0);

        if (month == 0 || year == 0)
            finish();


        if (payroll == null) {
            payroll = new Payroll();
            payroll.setYear(String.valueOf(year));
            payroll.setMonth(String.valueOf(month));

        }

    }

    public static void start(FragmentActivity context, long year, long month, long personelCode) {

        Intent starter = new Intent(context, PayrollFooterActivity.class);
        starter.putExtra(KEY_YEAR, year);
        starter.putExtra(KEY_MONTH, month);
        starter.putExtra(KEY_PERSONEL_CODE, personelCode);
        context.startActivity(starter);
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {


        private ArrayList<Payroll> payrolls;

        public ArrayList<Payroll> getPayrolls() {
            return payrolls;
        }

        public void setPayrolls(ArrayList<Payroll> payrolls) {
            this.payrolls = payrolls;
            notifyDataSetChanged();
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payrolls_item_grid, parent, false);
            FontChanger.applyMainFont(view);
            return new ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {

            if (holder == null)
                return;

            try {
                Payroll payroll = payrolls.get(position);

                holder.txtTitle.setText(payroll.getDesc());

                holder.txtPrice.setText("0");
                holder.txtNavigatePrice.setText("0");
                holder.txtNavigatePrice.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.txtPrice.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.lyDetails.setVisibility(View.GONE);
                holder.txtDetails.setText("");

                if (payroll.getTransactionType() == 1) {

                    holder.txtPrice.setBackgroundColor(Color.parseColor("#F1F8E9"));
                    holder.txtPrice.setText(NumberSeperator.separate(payroll.getAmount()));


                }
                if (payroll.getTransactionType() == -1) {

                    holder.txtNavigatePrice.setBackgroundColor(Color.parseColor("#ffebee"));
                    holder.txtNavigatePrice.setText(NumberSeperator.separate(payroll.getAmount()));


                    if (payroll.getTypePay() == 5) {
                        holder.txtDetails.setText("مانده: "+NumberSeperator.separate(payroll.getMande()));
                        holder.lyDetails.setVisibility(View.VISIBLE);
                    }
                }

                if (payroll.getTransactionType() == -10) {

                    holder.txtPrice.setBackgroundColor(Color.parseColor("#DCEDC8"));
                    holder.txtPrice.setText(NumberSeperator.separate(payroll.getAmount()));
                }
                if (payroll.getTransactionType() == -11) {

                    holder.txtNavigatePrice.setBackgroundColor(Color.parseColor("#FFCCBC"));
                    holder.txtNavigatePrice.setText(NumberSeperator.separate(payroll.getAmount()));
                }
                if (payroll.getTransactionType() == -12) {

                    if (payroll.getAmount() < 0) {
                        holder.txtNavigatePrice.setBackgroundColor(Color.parseColor("#FFE082"));
                        holder.txtNavigatePrice.setText(NumberSeperator.separate(payroll.getAmount()));
                    } else {
                        holder.txtPrice.setBackgroundColor(Color.parseColor("#FFE082"));
                        holder.txtPrice.setText(NumberSeperator.separate(payroll.getAmount()));
                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {

            return (payrolls == null || payrolls.size() == 0) ? 0 : payrolls.size();

        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            CardView cardView;
            TextView txtPrice;
            TextView txtTitle;
            TextView txtNavigatePrice;
            TextView txtDetails;
            LinearLayout lyDetails;


            public ItemHolder(View v) {
                super(v);

                cardView = (CardView) v.findViewById(R.id.cardView);

                cardView = v.findViewById(R.id.cardView);
                txtPrice = v.findViewById(R.id.txtPrice);
                txtTitle = v.findViewById(R.id.txtTitle);
                txtDetails = v.findViewById(R.id.txtDetails);
                lyDetails = v.findViewById(R.id.lyDetails);
                txtNavigatePrice = v.findViewById(R.id.txtNavigatePrice);

                FontChanger.applyMainFont(cardView);


            }
        }
    }


    private void MappingDate(ArrayList<Payroll> inputs) {

        ArrayList<Payroll> payrolls = new ArrayList<>();
        ArrayList<Payroll> negativePayroll = new ArrayList<>();

        Payroll nep = new Payroll();
        nep.setTransactionType(-11);
        nep.setDesc("جمع کسورات");

        Payroll pop = new Payroll();
        pop.setTransactionType(-10);
        pop.setDesc("جمع واریزی");


        Payroll top = new Payroll();
        top.setTransactionType(-12);
        top.setDesc("خالص پرداختی");


        if (inputs == null || inputs.size() == 0)
            return;

        for (int i = 0; i < inputs.size(); i++) {

            Payroll p = inputs.get(i);
            if (p.getTransactionType() == 1) {
                payrolls.add(p);
                pop.setAmount(pop.getAmount() + p.getAmount());
            } else {
                negativePayroll.add(p);
                nep.setAmount(nep.getAmount() + p.getAmount());
                //به این معناست که این کسری به دلیل پرداخت وام میباشد و باید مانده وام نمایش داده شود
//                if (p.getTypePay() == 5) {
//                    Payroll payroll = new Payroll();
//                    payroll.setDesc("مانده");
//                    payroll.setAmount(p.getMande());
//                    payroll.setTransactionType(p.getTransactionType());
//                    negativePayroll.add(payroll);
//
//                }
            }

        }

        payrolls.add(pop);
        if (negativePayroll.size() != 0)
            negativePayroll.add(nep);


        ArrayList<Payroll> total = new ArrayList<>();
        total.addAll(payrolls);
        total.addAll(negativePayroll);
        top.setAmount(pop.getAmount() - nep.getAmount());
        total.add(top);


        adapter.setPayrolls(total);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printLayout() {

        lyProgress.setVisibility(View.VISIBLE);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lyRoot);

        FontChanger.applyTitleFont(linearLayout);

        // convert view group to bitmap
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.buildDrawingCache();
        Bitmap bm = linearLayout.getDrawingCache();

        if (bm == null) {
            linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(linearLayout.getMeasuredWidth(),
                    linearLayout.getHeight(),
                    Bitmap.Config.ARGB_8888);

            Canvas c = new Canvas(bitmap);
            linearLayout.layout(linearLayout.getLeft(), linearLayout.getTop(), linearLayout.getRight(), linearLayout.getBottom());
            linearLayout.draw(c);
            bm = bitmap;

        }
        if (bm != null)
            createPDF(bm);

//        if (bm != null) {
//            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), true);
//            Bitmap[] imgs = new Bitmap[1];
//            imgs[0] = Bitmap.createBitmap(scaledBitmap, 0, 0, bm.getWidth(), (bm.getHeight()  )  );
//            createPDF(imgs);
//        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPDF(Bitmap layout) {

        // create a new document
        PdfDocument document = new PdfDocument();

        Bitmap bitmap = layout;

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        //Draw Image
        canvas.drawBitmap(bitmap, 0, 5, paint);
        document.finishPage(page);


        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Payroll_Pdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "factor.pdf";
        File filePath = new File(targetPdf);

        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            showPDF(filePath);


        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();


        lyProgress.setVisibility(View.GONE);
    }


    private void requestGetMessagePermission() {

        int smsPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (smsPermission == 0)
            return;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STOTAGE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STOTAGE);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STOTAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // if Permission Denied
                }
            }

        }
    }

    private void createCustomLayput() {

        ArrayList<Payroll> payrolls = adapter.getPayrolls();
        if (payrolls == null || payrolls.size() == 0)
            return;

        User user = SessionManagement.getInstance(getApplicationContext()).loadMember();

        customPrintLayout.addImage(R.drawable.ic_daryadelan_splash);
        int printTextSize = 11;

        customPrintLayout.addLeftAndRightTextRow("نام :", payrollUser.getFullName());
        customPrintLayout.addLeftAndRightTextRow("کد پرسنلی:", String.valueOf(personelCode));
        customPrintLayout.addLeftAndRightTextRow("تلفن :", user.getMobile());

        customPrintLayout.addEmptyRow(50);

        customPrintLayout.addLine(2);
        customPrintLayout.addTableFourRow("#", "عنوان", "پرداختی", "کسورات");
        customPrintLayout.addLine(0);

        customPrintLayout.addEmptyRow(50);
        customPrintLayout.addLine(0);

        for (int i = 0; i < payrolls.size(); i++) {

            Payroll payroll = payrolls.get(i);

            String rowNum = String.valueOf(i + 1);
            String name = payroll.getDesc();
            String positice = "0";
            String negative = "0";

            if (payroll.getTransactionType() == 1) {


                positice = String.valueOf((payroll.getAmount()));
                customPrintLayout.addTableFourRow(rowNum, name, positice, negative);
                customPrintLayout.addLine(0);

            }
            if (payroll.getTransactionType() == -1) {

                negative = String.valueOf((payroll.getAmount()));
                customPrintLayout.addTableFourRow(rowNum, name, positice, negative);
                if (payroll.getTypePay()==5){
                    customPrintLayout.addTableFourRow("-", "مانده"+NumberSeperator.separate(payroll.getMande()), "", "");
                }
                customPrintLayout.addLine(0);





            }

//            if (payroll.getTransactionType() == -10) {
//
//                positice = String.valueOf((payroll.getAmount()));
//            }
//            if (payroll.getTransactionType() == -11) {
//
//                negative = String.valueOf((payroll.getAmount()));
//            }
//            if (payroll.getTransactionType() == -12) {
//
//                if (payroll.getAmount() < 0) {
//                    negative = String.valueOf((payroll.getAmount()));
//                } else {
//                    positice = String.valueOf((payroll.getAmount()));
//                }
//
//            }


        }


        customPrintLayout.addEmptyRow(100);
        customPrintLayout.addLine(0);

        for (int i = 0; i < payrolls.size(); i++) {

            Payroll payroll = payrolls.get(i);

            String rowNum = String.valueOf(i + 1);
            String name = payroll.getDesc();
            String positice = "0";
            String negative = "0";


            if (payroll.getTransactionType() == -10) {

                positice = String.valueOf((payroll.getAmount()));
                customPrintLayout.addTableFourRow(String.valueOf(1), name, positice, negative);
                customPrintLayout.addLine(0);
            }
            if (payroll.getTransactionType() == -11) {

                negative = String.valueOf((payroll.getAmount()));
                customPrintLayout.addTableFourRow(String.valueOf(2), name, positice, negative);
                customPrintLayout.addLine(0);
            }
            if (payroll.getTransactionType() == -12) {
                txtTotalPrice.setText(NumberSeperator.separate(payroll.getAmount()) + " ریال ");
                if (payroll.getAmount() < 0) {
                    negative = String.valueOf((payroll.getAmount()));
                    customPrintLayout.addTableFourRow(String.valueOf(3), name, positice, negative);
                    customPrintLayout.addLine(0);
                } else {
                    positice = String.valueOf((payroll.getAmount()));
                    customPrintLayout.addTableFourRow(String.valueOf(3), name, positice, negative);
                    customPrintLayout.addLine(0);

                }

            }


        }
        customPrintLayout.addEmptyRow(700);


    }

    private void showPDF(File file) {

        if (file == null)
            return;

        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    private void getUserExtraInfos() {
        if (userRepo == null)
            return;

        userRepo.userInfo(getApplicationContext(), new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {
                if (answer == null)
                    return;

                try {
                    SessionManagement.getInstance(getApplicationContext()).saveMemberExtraInfo(PayrollFooterActivity.this, answer);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });
    }


    private void showError(String message) {
        new CustomDialogBuilder().showYesNOCustomAlert(PayrollFooterActivity.this, "خطا در دریافت اطلاعات", message, "تلاش مجدد", "انصراف", new CustomAlertDialog.OnActionClickListener() {
            @Override
            public void onClick(DialogFragment fragment) {
                getData();
                fragment.dismiss();

            }
        }, new CustomAlertDialog.OnCancelClickListener() {
            @Override
            public void onClickCancel(DialogFragment fragment) {
                fragment.dismiss();
            }

            @Override
            public void onClickOutside(DialogFragment fragment) {
                fragment.dismiss();
            }
        });
    }

}
