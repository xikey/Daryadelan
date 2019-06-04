package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;

public class LoginGuestActivity extends AppCompatActivity {

    private TextView txtUserName;
    private TextView txtPassword;
    private LinearLayout lyAction;
    private LinearLayout lyProgress;

    private IUser repo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guest);
    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, LoginGuestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initHeaderSize();
    }

    private void initHeaderSize() {

        RelativeLayout lyHeader = (RelativeLayout) findViewById(R.id.lyHeader);
        int width = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = lyHeader.getLayoutParams();
        int height = (width / 2);
        params.height = height;
        lyHeader.setLayoutParams(params);

    }
    private void initViews() {

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        lyAction = (LinearLayout) findViewById(R.id.lyAction);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);

        try {
            FontChanger.applyMainFont(findViewById(R.id.lyContent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        lyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomDialogBuilder().showYesNOCustomAlert(LoginGuestActivity.this, "ورود", "مایل به ارسال اطلاعات میباشد؟", "ارسال", "انصراف", new CustomAlertDialog.OnActionClickListener() {
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
            new CustomDialogBuilder().showAlert(LoginGuestActivity.this, "شماره تلفن همراه خالی میباشد");
            return;
        }





        user = new User();
        user.setMobile(txtUserName.getText().toString().trim());


    }

}
