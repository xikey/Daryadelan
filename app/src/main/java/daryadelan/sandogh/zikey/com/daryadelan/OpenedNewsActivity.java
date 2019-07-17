package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import daryadelan.sandogh.zikey.com.daryadelan.model.News;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class OpenedNewsActivity extends AppCompatActivity {

    private static final String KEY_PARCABLE = "PARCABLE";

    private RelativeLayout lyProgress;
    private RelativeLayout lyContent;
    private TextView txtDate;

    private AppBarLayout appbar;
    private CollapsingToolbarLayout toolbar_layout;
    private Toolbar toolbar;
    private Toolbar toolbar_colored;
    private ImageView imgNews;

    private News news;
    private TextView txtDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_news);

        parseIntent();
        initToolbar();
        initViews();
        initCollapsingToolbarLayout();
        initContent();


    }

    private void initContent() {
        if (news == null)
            return;

        txtDate.setText(news.getPersianDate());
        txtDetails.setText(news.getPostBody());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtDetails.setText(Html.fromHtml(news.getPostBody(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtDetails.setText(Html.fromHtml(news.getPostBody()));
        }

        String url = BuildConfig.IPAddress + "/" + news.getPostImage();

        new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(imgNews).load();

    }

    private void parseIntent() {

        Intent data = getIntent();
        if (data == null)
            return;

        if (data.hasExtra(KEY_PARCABLE))
            news = data.getExtras().getParcelable(KEY_PARCABLE);

        if (news == null) {
            Toasty.error(OpenedNewsActivity.this, "خطا در دریافت اطلاعات خبر");
            return;
        }

    }


    private void initCollapsingToolbarLayout() {

        if (appbar == null)
            return;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                appbar.setExpanded(true, true);
            }
        }, 900);

        toolbar_layout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbar_layout.setExpandedTitleTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile_Bold.ttf"));

        int width = getResources().getDisplayMetrics().widthPixels;


        final ViewGroup.LayoutParams params = appbar.getLayoutParams();
        params.height = (width * 2 / 3);
        appbar.setLayoutParams(params);

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                float val = (1 - percentage);
                float val2 = 1 - percentage;
                if (val < 0.7)
                    val = 0;

                if (val < 1)
                    val = val / 2;

                if (percentage == 1) {
                    toolbar_colored.setAlpha(1);
                    toolbar.setAlpha(0);


                } else {
                    toolbar_colored.setAlpha(0);
                    toolbar.setAlpha(1);

                }

                txtDate.setAlpha(val);
                imgNews.setAlpha(val2);
            }
        });

    }

    private void initViews() {

        FontChanger.applyMainFont(findViewById(R.id.lyContent));
        lyContent = (RelativeLayout) findViewById(R.id.lyContent);

        txtDate = (TextView) findViewById(R.id.txtDate);
        appbar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_colored = (Toolbar) findViewById(R.id.toolbar_colored);

        imgNews = (ImageView) findViewById(R.id.imgNews);
        txtDetails = (TextView) findViewById(R.id.txtDetails);
        FontChanger.applyTitleFont(txtDate);
        FontChanger.applyMainFont(txtDetails);


    }

    public static void start(FragmentActivity context, News news) {
        Intent starter = new Intent(context, OpenedNewsActivity.class);
        starter.putExtra(KEY_PARCABLE, news);
        context.startActivity(starter);
    }

    private void initToolbar() {


        String title = news.getPostTitle();


        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, title, news.getPostTitle(), null);

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar_colored, null, null, null);
    }

}
