package daryadelan.sandogh.zikey.com.daryadelan;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;

public class ZikeySplashActivity extends AppCompatActivity {

    private ConstraintLayout layoutRoot;
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 4000;
    private int isUserLoggedinBefore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_zikey_splash);

        initUserLoggedinHistory();
        initViews();
        initAnimation();
        runHandler();

    }

    private void initUserLoggedinHistory() {
        isUserLoggedinBefore   = SessionManagement.getInstance(getApplicationContext()).getIsUserLoggedInBefore();
    }

    //جهت تعیین زمان نمایش صفحه خوشامدگویی
    private void runHandler() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedinBefore == 0)
                    SigninActivity.start(ZikeySplashActivity.this);
                else {
                    LoginActivity.start(ZikeySplashActivity.this);
                }
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }


    private void initViews() {

        layoutRoot = (ConstraintLayout) findViewById(R.id.layoutRoot);
        FontChanger.applyTitleFont(findViewById(R.id.txtName));
    }

    private void initAnimation() {

        AnimationDrawable animationDrawable = (AnimationDrawable) layoutRoot.getBackground();
        animationDrawable.setEnterFadeDuration(10);
        animationDrawable.setExitFadeDuration(1800);
        animationDrawable.start();

    }


    private void hideStatusBar() {

        try {

            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE
                                // Set the content to appear under the system bars so that the
                                // content doesn't resize when the system bars hide and show.
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                // Hide the nav bar and status bar
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
