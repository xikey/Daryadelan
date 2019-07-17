package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.razanpardazesh.razanlibs.Tools.Convertor;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Advertise;
import daryadelan.sandogh.zikey.com.daryadelan.model.Photo;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AdvertiseWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IAdvertise;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.AdvertiseServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.AutoFitGridLayoutManager;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class PhotoViewActivity extends AppCompatActivity {

    private static final String KEY_PARCABLE = "PARCABLE";

    private ImageView imgFullPhoto;
    private Photo gallery;
    private RecyclerView rvItems;
    private AutoFitGridLayoutManager gridLayoutManager;
    private ImagesAdapter adapter;
    private LinearLayout lyProgress;

    private IAdvertise repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        initToolbar();
        initRepo();
        parseIntent();
        initViews();
        initRecycleView();
        initContent();
        getData();


    }

    private void getData() {

        User user = UserInstance.getInstance().getUser();
        if (user == null) {
            showError("خطا در دریافت اطلاعات کاربری", true);
            return;
        }

        if (gallery == null || TextUtils.isEmpty(gallery.getGalleryName())) {
            showError("خطا در دریافت اطلاعات  ", true);
            return;
        }

        lyProgress.setVisibility(View.VISIBLE);

        repo.getGallary(getApplicationContext(), gallery.getGalleryName(), user.getTokenType(), user.getToken(), new IRepoCallBack<AdvertiseWrapper>() {
            @Override
            public void onAnswer(AdvertiseWrapper answer) {

                lyProgress.setVisibility(View.GONE);
                if (answer != null && answer.getData() != null && answer.getData().size() != 0) {
                    adapter.setImages(answer.getData());
                } else {
                    // TODO: 7/17/2019 Nothing .!.??
                }

            }

            @Override
            public void onError(Throwable error) {
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

    private void initRepo() {

        if (repo == null)
            repo = new AdvertiseServerRepo();
    }

    private void initContent() {
        if (gallery == null)
            return;

        String url = BuildConfig.IPAddress + "/" + gallery.getGalleryImage();
        new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(imgFullPhoto).load();

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

    private void initViews() {

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        imgFullPhoto = (ImageView) findViewById(R.id.imgFullPhoto);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);


        int width = getResources().getDisplayMetrics().widthPixels;

        final ViewGroup.LayoutParams params = imgFullPhoto.getLayoutParams();
        params.height = (width * 2 / 3);
        imgFullPhoto.setLayoutParams(params);

    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, " ", null, null);

    }

    private void parseIntent() {

        Intent data = getIntent();
        if (data == null)
            return;

        if (data.hasExtra(KEY_PARCABLE))
            gallery = data.getExtras().getParcelable(KEY_PARCABLE);

        if (gallery == null) {
            Toasty.error(PhotoViewActivity.this, "خطا در دریافت اطلاعات گالری");
            return;
        }


    }


    public static void start(FragmentActivity context, Photo gallery) {
        Intent starter = new Intent(context, PhotoViewActivity.class);
        starter.putExtra(KEY_PARCABLE, gallery);
        context.startActivity(starter);
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

            holder.photo=photo;


        }


        @Override
        public int getItemCount() {

            if (images == null || images.size() == 0)
                return 0;


            return images.size();


        }


        private class PhotoHolder extends RecyclerView.ViewHolder {

            ImageView image;
            Advertise photo;


            public PhotoHolder(View v) {
                super(v);

                image = v.findViewById(R.id.image);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = BuildConfig.IPAddress + "/" + photo.getImagepath();
                        new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(imgFullPhoto).load();


                    }
                });


            }
        }


    }

    private void showError(String message, boolean finishApp) {

        new CustomDialogBuilder().showAlert(PhotoViewActivity.this, message, new CustomAlertDialog.OnCancelClickListener() {
            @Override
            public void onClickCancel(DialogFragment fragment) {

                fragment.dismiss();
                if (finishApp)
                    finish();

            }

            @Override
            public void onClickOutside(DialogFragment fragment) {

                fragment.dismiss();
                if (finishApp)
                    finish();
            }
        });


    }

}
