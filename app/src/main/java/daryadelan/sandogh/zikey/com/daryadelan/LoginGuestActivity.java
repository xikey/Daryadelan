package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class LoginGuestActivity extends AppCompatActivity {

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

}
