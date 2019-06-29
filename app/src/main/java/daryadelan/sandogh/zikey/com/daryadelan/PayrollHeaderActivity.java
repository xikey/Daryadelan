package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.Payroll;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PayrollWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPayroll;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.PayrollServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;

public class PayrollHeaderActivity extends AppCompatActivity {

    private IPayroll repo;
    private LinearLayout lyProgress;


    private CardView lyPickYear;
    private CardView lyMonth;
    private CardView lyPersonelCode;
    private LinearLayout lyAction;
    private EditText edtYear;
    private EditText edtMonth;
    private EditText edtPersonelCode;

    private Payroll selectedPayroll;

    //تمامی فیش های دریافت شده
    private ArrayList<Payroll> payrolls;

    private ArrayList<Payroll> filteredPayrolls;

    private ImageView imgBackground;

    private User user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll_header);

        getUserData();
        initToolbar();
        initRepo();
        initViews();
        getData();
        initListeners();

    }

    private void getUserData() {
        user=SessionManagement.getInstance(getApplicationContext()).loadMember();
    }

    private void initListeners() {
        lyPickYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYear();
            }
        });

        edtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYear();
            }
        });

        edtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth();
            }
        });

        lyMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth();
            }
        });

        lyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtMonth.getText())){
                    new CustomDialogBuilder().showAlert(PayrollHeaderActivity.this,"فیلد ماه نامعتبر میباشد!");
                    return;
                }

                if (selectedPayroll != null)
                    PayrollFooterActivity.start(PayrollHeaderActivity.this, Long.parseLong(selectedPayroll.getYear()), Long.parseLong(selectedPayroll.getMonth()),user.getPersonalCode());
            }
        });

        lyPersonelCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = SessionManagement.getInstance(getApplicationContext()).loadMember();
                if (!user.getPersonType().equals("su")){
                    new CustomDialogBuilder().showAlert(PayrollHeaderActivity.this,"امکان تغییر کد پرسنلی برای شما وجود ندارد");
                }
                else {
                    changePersonelCode();
                }
            }
        });

        edtPersonelCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = SessionManagement.getInstance(getApplicationContext()).loadMember();
                if (!user.getPersonType().equals("su")){
                    new CustomDialogBuilder().showAlert(PayrollHeaderActivity.this,"امکان تغییر کد پرسنلی برای شما وجود ندارد");
                }
                else {
                    changePersonelCode();
                }
            }
        });
    }

    private void getData() {
        if (repo == null)
            return;

        if (user==null){
            new CustomDialogBuilder().showAlert(PayrollHeaderActivity.this,"خطا در دریافت اطلاعات کاربری");
            return;
        }

        lyProgress.setVisibility(View.VISIBLE);

        repo.allAvailablePayrolls(getApplicationContext(), user.getPersonalCode(),new IRepoCallBack<PayrollWrapper>() {
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

                payrolls = answer.getData();
                filteredPayrolls = new ArrayList<>();
                filteredPayrolls.addAll(payrolls);

                mapData();
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

    private void mapData() {

        if (filteredPayrolls == null || filteredPayrolls.size() == 0)
            return;

        selectedPayroll = filteredPayrolls.get(filteredPayrolls.size() - 1);
        initContent();


    }

    private void initRepo() {

        if (repo == null)
            repo = new PayrollServerRepo();

    }

    private void initViews() {

        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);

        lyPickYear = (CardView) findViewById(R.id.lyPickYear);
        lyMonth = (CardView) findViewById(R.id.lyMonth);
        lyPersonelCode = (CardView) findViewById(R.id.lyPersonelCode);

        lyAction = (LinearLayout) findViewById(R.id.lyAction);

        edtYear = (EditText) findViewById(R.id.edtYear);
        edtMonth = (EditText) findViewById(R.id.edtMonth);
        edtPersonelCode = (EditText) findViewById(R.id.edtPersonelCode);
        imgBackground = (ImageView) findViewById(R.id.imgBackground);

        if (user!=null){
            edtPersonelCode.setText(""+user.getPersonalCode());
        }

        new ImageViewWrapper(getApplicationContext()).into(imgBackground).loadBlur(R.drawable.bg_calendar);

        try {
            FontChanger.applyMainFont(findViewById(R.id.lyRoot));
//            FontChanger.applyTitleFont(findViewById(R.id.txtDateHeader));
            FontChanger.applyTitleFont(findViewById(R.id.lyAction));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_payroll_header), null);

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, PayrollHeaderActivity.class);
        context.startActivity(starter);
    }

    private void initContent() {

        if (selectedPayroll == null)
            return;

        try {

            edtYear.setText(selectedPayroll.getYear());
            edtMonth.setText(selectedPayroll.getMonthAsString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectYear() {

        if (payrolls == null || payrolls.size() == 0)
            return;

        final ArrayList<Payroll> availablePayrolls = new ArrayList<>();

        for (int i = 0; i < payrolls.size(); i++) {

            if (i == 0)
                availablePayrolls.add(payrolls.get(0));
            else {
                boolean available = true;
                for (int j = 0; j < availablePayrolls.size(); j++) {
                    if (payrolls.get(i).getYear().equals(availablePayrolls.get(j).getYear()))
                        available = false;
                }
                if (available)
                    availablePayrolls.add(payrolls.get(i));
            }

        }

        CharSequence items[] = new CharSequence[availablePayrolls.size()];

        for (int i = 0; i < availablePayrolls.size(); i++) {
            items[i] = availablePayrolls.get(i).getYear();
        }

        try {
            new CustomDialogBuilder().showPickerDialog(PayrollHeaderActivity.this, "انتخاب سال", items, new CustomDialogBuilder.OnPickerDialogListener() {
                @Override
                public void onPick(int position) {
                    selectedPayroll = availablePayrolls.get(position);
                    filteredPayrolls = availablePayrolls;
                    initContent();
                }
            });
        } catch (Exception ex) {
            LogWrapper.loge("ChequesActivity_onClick_Exception: ", ex);
        }

    }

    private void selectMonth() {

        if (payrolls == null || payrolls.size() == 0)
            return;

        final ArrayList<Payroll> availablePayrolls = new ArrayList<>();

        for (int i = 0; i < payrolls.size(); i++) {
            if (selectedPayroll.getYear() .equals( payrolls.get(i).getYear()))
                availablePayrolls.add(payrolls.get(i));
        }

        if (availablePayrolls.size() == 0)
            return;

        CharSequence items[] = new CharSequence[availablePayrolls.size()];

        for (int i = 0; i < availablePayrolls.size(); i++) {
            items[i] = availablePayrolls.get(i).getMonthAsString();
        }

        try {
            new CustomDialogBuilder().showPickerDialog(PayrollHeaderActivity.this, "انتخاب ماه", items, new CustomDialogBuilder.OnPickerDialogListener() {
                @Override
                public void onPick(int position) {
                    selectedPayroll = availablePayrolls.get(position);
                    initContent();
                }
            });
        } catch (Exception ex) {
            LogWrapper.loge("ChequesActivity_onClick_Exception: ", ex);
        }

    }

    private void changePersonelCode(){
        new CustomDialogBuilder().showInputTextDialog(PayrollHeaderActivity.this, "کد پرسنلی",  new CustomDialogBuilder.OnDialogListener() {
            @Override
            public void onOK(String input) {
                try{
                    long pc= Long.parseLong(input);
                    if (pc!=0){
                        user.setPersonalCode(pc);
                        edtPersonelCode.setText(input);
                        payrolls=null;
                        filteredPayrolls=null;
                        getData();
                    }

                }catch (Exception e){
                    new CustomDialogBuilder().showAlert(PayrollHeaderActivity.this,"کد پرسنلی نامعتبر");
                    e.printStackTrace();
                }
            }

            @Override
            public void onNeutral(String input) {

            }
        });
    }

    private void showError(String message) {
        edtMonth.setText("");
        edtYear.setText("");
        new CustomDialogBuilder().showYesNOCustomAlert(PayrollHeaderActivity.this, "خطا در دریافت اطلاعات", message, "تلاش مجدد", "انصراف", new CustomAlertDialog.OnActionClickListener() {
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