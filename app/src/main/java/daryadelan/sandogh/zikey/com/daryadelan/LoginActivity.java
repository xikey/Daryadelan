package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import daryadelan.sandogh.zikey.com.daryadelan.customview.Indicator;
import daryadelan.sandogh.zikey.com.daryadelan.customview.PageFragment;

public class LoginActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initHeaderSize();
    }

    public static void start(FragmentActivity context) {
    Intent starter = new Intent(context, LoginActivity.class);
    context.startActivity(starter);
}


    private void initHeaderSize() {

        RelativeLayout lyHeader= (RelativeLayout) findViewById(R.id.lyHeader);
        int width = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = lyHeader.getLayoutParams();
        int height = (width / 2);
        params.height = height;
        lyHeader.setLayoutParams(params);

    }


}
