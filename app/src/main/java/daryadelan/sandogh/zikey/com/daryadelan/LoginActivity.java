package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.UserServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;

public class LoginActivity extends AppCompatActivity {


    private TextView txtUserName;
    private TextView txtPassword;
    private CardView lyAction;
    private LinearLayout lyProgress;

    private IUser repo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_2);

        initViews();
        initListeners();
        initRepo();
    }

    private void initRepo() {

        if (repo == null)
            repo = new UserServerRepo();
    }

    private void initListeners() {
        lyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomDialogBuilder().showYesNOCustomAlert(LoginActivity.this, "ورود", "مایل به ارسال اطلاعات میباشد؟", "ارسال", "انصراف", new CustomAlertDialog.OnActionClickListener() {
                    @Override
                    public void onClick(DialogFragment fragment) {
                        initValidation();
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
        });
    }

    private void initValidation() {

        if (TextUtils.isEmpty(txtUserName.getText())) {
            new CustomDialogBuilder().showAlert(LoginActivity.this, "شماره تلفن همراه خالی میباشد");
            return;
        }

        if (TextUtils.isEmpty(txtPassword.getText())) {
            new CustomDialogBuilder().showAlert(LoginActivity.this, "رمز عبور خالی میباشد");
            return;
        }

        if (txtPassword.getText().length() < 6) {
            new CustomDialogBuilder().showAlert(LoginActivity.this, "حد اقل مقدار رمز عبور 6 رقم میباشد");
            return;
        }
        if (txtPassword.getText().toString().contains(" ")) {
            new CustomDialogBuilder().showAlert(LoginActivity.this, "مقدار رمز عبور نمیتواند شامل فضای خالی (فاصله) باشد");
            return;
        }


        user = new User();
        user.setMobile(txtUserName.getText().toString().trim());
        user.setPassword(txtPassword.getText().toString().trim());
        sendData();

    }

    private void sendData() {
        if (repo == null)
            return;

        if (user == null)
            return;

        lyProgress.setVisibility(View.VISIBLE);

        repo.login(LoginActivity.this, user, new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {
                lyProgress.setVisibility(View.GONE);


                if (answer == null) {
                    new CustomDialogBuilder().showAlert(LoginActivity.this, getString(R.string.error_login_please_try_again));
                    return;
                }

                if (TextUtils.isEmpty(answer.getToken())) {
                    new CustomDialogBuilder().showAlert(LoginActivity.this, getString(R.string.error_login_please_try_again));
                    return;
                }

                user.setToken(answer.getToken());
                user.setTokenType(answer.getTokenType());
                user.setTokenExpireDate(answer.getTokenExpireDate());

// TODO: 5/31/2019 TOKEN IS BASE 64. MUST GET User FirstName and LastName From Token
                if (SessionManagement.getInstance(getApplicationContext()).saveMemberData(LoginActivity.this, user)) {

                    setResult(RESULT_OK);
                    MainActivity.start(LoginActivity.this);
                    finish();

                }


            }

            @Override
            public void onError(Throwable error) {
                new CustomDialogBuilder().showAlert(LoginActivity.this, error.getMessage().toString());
                lyProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });
    }

    private void initViews() {

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        lyAction = (CardView) findViewById(R.id.lyAction);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);

        try {
            FontChanger.applyMainFont(findViewById(R.id.lyContent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initHeaderSize();
    }

    public static void start(FragmentActivity context, int requestCode) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    private void initHeaderSize() {

        RelativeLayout lyHeader = (RelativeLayout) findViewById(R.id.lyHeader);
        int width = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = lyHeader.getLayoutParams();
        int height = ((width/2));
        params.height = height;
        lyHeader.setLayoutParams(params);

    }


}
