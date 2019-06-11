package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;



public class SplashActivity extends AwesomeSplash {


    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.colorPrimaryDark); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(800); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_TOP);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ic_daryadelan_splash); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Title
        configSplash.setTitleSplash("صندوق بازنشستگی و وظیفه مستخدمین سازمان بنادر و دریانوردی");
        configSplash.setTitleTextColor(R.color.colorWhite);
        configSplash.setTitleTextSize(10f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/IRYekan.ttf");



    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, SplashActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void animationsFinished() {
        SigninActivity.start(this);
        overridePendingTransition(0, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void initUI(int flag) {
        super.initUI(flag);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        LinearLayout l = (LinearLayout) inflater.inflate(R.layout.layout_splash_progress, null);

        addContentView(l, new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT ,LinearLayout.LayoutParams.MATCH_PARENT ) );
    }
}
