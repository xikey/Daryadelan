package daryadelan.sandogh.zikey.com.daryadelan;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.UserServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.sqlite.UserSqliteRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;

public class LoginActivity extends AppCompatActivity {


    private TextView txtUserName;
    private TextView txtPassword;
    private CardView lyAction;
    private CardView lySignin;
    private LinearLayout lyProgress;

    private IUser repo;
    private User user;

    private IUser userSqliteRepo;

    private AnimatorSet shirinkLogo;
    private AnimatorSet globalAnimate;
    private CardView crdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }

        setContentView(R.layout.activity_login_2);

        initViews();
        initListeners();
        initRepo();


    }

    private void initRepo() {

        if (repo == null)
            repo = new UserServerRepo();

        if (userSqliteRepo == null)
            userSqliteRepo = new UserSqliteRepo();
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

        lySignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninActivity.start_clearDB(LoginActivity.this );
                finish();
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
        crdContainer.setVisibility(View.GONE);

        repo.login(LoginActivity.this, user, new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {
                lyProgress.setVisibility(View.GONE);
                crdContainer.setVisibility(View.VISIBLE);


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

                if (userSqliteRepo != null)
                    userSqliteRepo.saveUserDatas(getApplicationContext(), user, new IRepoCallBack<User>() {
                        @Override
                        public void onAnswer(User answer) {

                            if (answer == null || answer.getResultId() < 0) {
                                new CustomDialogBuilder().showAlert(LoginActivity.this, "خطا در ذخیره سازی اطلاعات کاربر، لطفا مجددا تلاش نمایید!");
                                return;
                            }

                            setResult(RESULT_OK);
                            MainActivity.start(LoginActivity.this);
                            finish();
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

            @Override
            public void onError(Throwable error) {
                new CustomDialogBuilder().showAlert(LoginActivity.this, error.getMessage().toString());
                lyProgress.setVisibility(View.GONE);
                crdContainer.setVisibility(View.VISIBLE);

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
        lySignin = (CardView) findViewById(R.id.lySignin);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        crdContainer = (CardView) findViewById(R.id.crdContainer);

        try {
            FontChanger.applyMainFont(findViewById(R.id.lyContent));

        } catch (Exception e) {
            e.printStackTrace();
        }

        animateLogo();

    }

    @Override
    protected void onStart() {
        super.onStart();
        clearSession();
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

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    private void initHeaderSize() {


        try {
            RelativeLayout lyHeader = (RelativeLayout) findViewById(R.id.lyHeader);
            int width = getResources().getDisplayMetrics().widthPixels;
            ViewGroup.LayoutParams params = lyHeader.getLayoutParams();
            int height = ((width));
            params.height = height;
            lyHeader.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int widthPixels = metrics.widthPixels;
            int heightPixels = metrics.heightPixels;
            float scaleFactor = metrics.density;
            float widthDp = widthPixels / scaleFactor;
            float heightDp = heightPixels / scaleFactor;
            float smallestWidth = Math.min(widthDp, heightDp);

            // is Tablet?
            if (smallestWidth > 600) {
                int height = getResources().getDisplayMetrics().heightPixels;
                int width = getResources().getDisplayMetrics().widthPixels;
                ViewGroup.LayoutParams params = crdContainer.getLayoutParams();
                params.height = height / 2;
                crdContainer.setLayoutParams(params);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void clearSession() {

        if (userSqliteRepo != null)
            userSqliteRepo.exitApp(getApplicationContext(), new IRepoCallBack<User>() {
                @Override
                public void onAnswer(User answer) {

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

    private void animateLogo() {

        shirinkLogo = new AnimatorSet();
//        Animator animatorShirinkX = ObjectAnimator.ofFloat(crdContainer, "scaleX", 1f, 0.9f);
//        animatorShirinkX.setDuration(50);
//        animatorShirinkX.setInterpolator(new DecelerateInterpolator());
//
//        Animator animatorShirinkY = ObjectAnimator.ofFloat(crdContainer, "scaleY", 1f, 0.9f);
//        animatorShirinkY.setDuration(50);
//        animatorShirinkY.setInterpolator(new DecelerateInterpolator());
//
//        shirinkLogo.playTogether(animatorShirinkX, animatorShirinkY);


        AnimatorSet setGrow = new AnimatorSet();
        Animator animatorGrowX = ObjectAnimator.ofFloat(crdContainer, "scaleX", 0.9f, 1.1f);
        animatorGrowX.setDuration(200);
        animatorGrowX.setInterpolator(new AccelerateInterpolator());

        Animator animatorGrowY = ObjectAnimator.ofFloat(crdContainer, "scaleY", 0.9f, 1.1f);
        animatorGrowY.setDuration(200);
        animatorGrowY.setInterpolator(new AccelerateInterpolator());

        setGrow.playTogether(animatorGrowX, animatorGrowY);


        AnimatorSet setFix = new AnimatorSet();
        Animator setFixX = ObjectAnimator.ofFloat(crdContainer, "scaleX", 1.1f, 1f);
        setFixX.setDuration(800);
        setFixX.setInterpolator(new BounceInterpolator());

        Animator setFixY = ObjectAnimator.ofFloat(crdContainer, "scaleY", 1.1f, 1f);
        setFixY.setDuration(800);
        setFixY.setInterpolator(new BounceInterpolator());
        setFix.playTogether(setFixX, setFixY);

        globalAnimate = new AnimatorSet();
        globalAnimate.playSequentially(shirinkLogo, setGrow, setFix);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(globalAnimate);
        animatorSet.setStartDelay(50);
        animatorSet.start();


    }


}
