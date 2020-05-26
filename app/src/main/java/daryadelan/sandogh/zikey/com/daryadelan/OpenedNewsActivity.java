package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.razanpardazesh.razanlibs.Tools.Convertor;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Advertise;
import daryadelan.sandogh.zikey.com.daryadelan.model.News;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AdvertiseWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IAdvertise;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.AdvertiseServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.AutoFitGridLayoutManager;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class OpenedNewsActivity extends AppCompatActivity {

    private static final String KEY_PARCABLE = "PARCABLE";

    private RelativeLayout lyProgress;
    private RelativeLayout lyContent;
    private TextView txtDate;
    private RecyclerView rvItems;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout toolbar_layout;
    private Toolbar toolbar;
    private Toolbar toolbar_colored;
    private ImageView imgNews;
    private AutoFitGridLayoutManager gridLayoutManager;
    private News news;
    private TextView txtDetails;

    private ImagesAdapter adapter;
    private IAdvertise repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_news);

        parseIntent();
        initRepo();
        initToolbar();
        initViews();
        initCollapsingToolbarLayout();
        initContent();
        initRecycleView();


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

        if (!TextUtils.isEmpty(news.getGalleryName())) {
            getImages(news.getGalleryName());
        }

    }

    private void initRepo() {

        if (repo == null)
            repo = new AdvertiseServerRepo();
    }

    private void initRecycleView() {

        int gridWidth = Convertor.toPixcel(120, getApplicationContext());

        if (gridLayoutManager == null)
            gridLayoutManager = new AutoFitGridLayoutManager(this, gridWidth);


        rvItems.setLayoutManager(gridLayoutManager);

        if (adapter == null)
            adapter = new ImagesAdapter();

        rvItems.setAdapter(adapter);


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
        rvItems = (RecyclerView) findViewById(R.id.rvItems);

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

    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.PhotoHolder> {

        private ArrayList<Advertise> images;


        public void setImages(ArrayList<Advertise> images) {
            this.images = images;
            notifyDataSetChanged();
        }

        @Override
        public ImagesAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (parent == null || parent.getContext() == null)
                return null;


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_images_grid_item, parent, false);
            PhotoHolder holder = new PhotoHolder(view);
            return holder;

        }

        @Override
        public void onBindViewHolder(ImagesAdapter.PhotoHolder holder, int position) {

            if (holder == null)
                return;

            Advertise photo = images.get(position);
            if (photo == null)
                return;

            String url = BuildConfig.IPAddress + "/" + photo.getImagepath();
            new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(holder.image).load();

            holder.photo = photo;


        }


        @Override
        public int getItemCount() {

            if (images == null || images.size() == 0)
                return 0;


            return images.size();


        }


        public class PhotoHolder extends RecyclerView.ViewHolder {

            ImageView image;
            Advertise photo;


            public PhotoHolder(View v) {
                super(v);

                image = v.findViewById(R.id.image);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        String url = BuildConfig.IPAddress + "/" + photo.getImagepath();
//                        new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(imgFullPhoto).load();


                    }
                });


            }
        }


    }

    private void getImages(String galleryName) {

        User user = UserInstance.getInstance().getUser();
        if (user == null) {
            return;
        }

        if (TextUtils.isEmpty(galleryName)) {
            return;
        }

        repo.getGallary(getApplicationContext(), galleryName, user.getTokenType(), user.getToken(), new IRepoCallBack<AdvertiseWrapper>() {
            @Override
            public void onAnswer(AdvertiseWrapper answer) {


                if (answer != null && answer.getData() != null && answer.getData().size() != 0) {
                    adapter.setImages(answer.getData());
                } else {
                    // TODO: 7/17/2019 Nothing .!.??
                }

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


    

}
