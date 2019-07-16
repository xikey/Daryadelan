package daryadelan.sandogh.zikey.com.daryadelan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;

public class PhotoViewActivity extends AppCompatActivity {

    private ImageView imgFullPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        initToolbar();


    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, " ", null, null);

    }

}
