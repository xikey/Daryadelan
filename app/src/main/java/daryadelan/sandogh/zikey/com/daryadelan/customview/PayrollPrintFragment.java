package daryadelan.sandogh.zikey.com.daryadelan.customview;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.CustomPrintLayout;
import daryadelan.sandogh.zikey.com.daryadelan.R;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Payroll;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.NumberSeperator;


/**
 * Created by Zikey on 23/10/2017.
 */

public class PayrollPrintFragment extends DialogFragment {

    private static final String KEY_ALERT_DIALOG = "ALERT_DIALOG";

    private PayrollPrintFragment dialog;


    //Views
    private ViewGroup root;
    private RelativeLayout lyContent;
    private RelativeLayout lyBackground;

    private ArrayList<Payroll> payrolls;

    private LinearLayout lyRoot;
    private CustomPrintLayout customPrintLayout;
    private User payrollUser;

    private View.OnClickListener openPdfFileClickLister;


    public PayrollPrintFragment setPayrolls(ArrayList<Payroll> payrolls) {
        this.payrolls = payrolls;
        return this;
    }

    public PayrollPrintFragment setPayrollUser(User payrollUser) {
        this.payrollUser = payrollUser;
        return this;
    }

    public PayrollPrintFragment setDialog(PayrollPrintFragment dialog) {
        this.dialog = dialog;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_FRAME,
                android.R.style.Theme_Black_NoTitleBar);
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));

        }

        dialog.setCancelable(false);

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.layout_payroll_print_fragment, null);
        if (root == null)
            return null;

        initDialog();
        initViews();
        initContent();
        initClickListeners();
        createCustomLayput();

        return root;

    }


    private void initClickListeners() {

        if (openPdfFileClickLister == null)
            openPdfFileClickLister = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        printLayout();
                    }


                }
            };


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printLayout() {


        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.lyRoot);

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
        // TODO: 7/31/2019 DATE MSUT SET HERE
        String filename = "payroll_" + payrollUser.getPersonalCode() + "_DATE.pdf";
        String targetPdf = directory_path + filename;
        File filePath = new File(targetPdf);

        try {
            document.writeTo(new FileOutputStream(filePath));
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "فایل فیش حقوقی شما در مسیر زیر ذخیره شد");
            showPDF(filePath);


        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(getActivity(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();


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


    private void initContent() {

    }

    private void initDialog() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    void initViews() {

        lyContent = (RelativeLayout) root.findViewById(R.id.lyContent);

        lyBackground = (RelativeLayout) root.findViewById(R.id.lyBackground);

        lyRoot = root.findViewById(R.id.lyRoot);
        customPrintLayout = root.findViewById(R.id.customPrintLayout);

        FontChanger.applyMainFont(lyContent);

    }


    public static PayrollPrintFragment Show(FragmentActivity act, ArrayList<Payroll> payrolls, User user) {
        try {

            if (user == null || payrolls == null)
                return null;

            PayrollPrintFragment fragment = new PayrollPrintFragment();

            fragment.setPayrolls(payrolls)
                    .setPayrollUser(user)
                    .setDialog(fragment);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!act.isDestroyed())
                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
            } else {
                if (!act.isFinishing())
                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
            }

            return fragment;
        } catch (Exception ex) {

            LogWrapper.loge("VisitorsAlertDialog_Show_Exception: ", ex);
        }
        return null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnActionClickListener {

        void onClick(DialogFragment fragment);

    }

    private void createCustomLayput() {

        if (payrolls == null || payrolls.size() == 0)
            return;

        User user = UserInstance.getInstance().getUser();

        customPrintLayout.addImage(R.drawable.ic_daryadelan_splash);
        int printTextSize = 11;

        customPrintLayout.addLeftAndRightTextRow("نام :", payrollUser.getFullName());
        customPrintLayout.addLeftAndRightTextRow("کد پرسنلی:", String.valueOf(payrollUser.getPersonalCode()));
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
                if (payroll.getTypePay() == 5) {
                    customPrintLayout.addTableFourRow("-", "مانده" + NumberSeperator.separate(payroll.getMande()), "", "");
                }
                customPrintLayout.addLine(0);


            }


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


}
