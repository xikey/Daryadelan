package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Photo;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PhotosWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPhotos;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.PhotosServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class GalleriesActivity extends AppCompatActivity {

    private RecyclerView rvItem;
    private ItemAdapter adapter;
    private LinearLayout lyProgress;
    private LinearLayout lyBottomProgress;
    private IPhotos photosRepo;
    private User user;
    private long pageCount = 0;
    private int hasMore = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleries);

        initRepo();
        initToolbar();
        getUserData();
        initViews();
        initRecycleView();
        getNews();

    }


    private void getUserData() {
        user = UserInstance.getInstance().getUser();
        if (user == null) {
            Toasty.error(GalleriesActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }
    }

    private void initRepo() {

        if (photosRepo == null)
            photosRepo = new PhotosServerRepo();

    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_galleries), null);
    }

    private void initViews() {


        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        lyBottomProgress = (LinearLayout) findViewById(R.id.lyBottomProgress);
        lyProgress.setVisibility(View.VISIBLE);

    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(GalleriesActivity.this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);

        rvItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (hasMore == 0)
                    return;

                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int pos = layoutManager.findLastVisibleItemPosition();
                    if (adapter != null) {
                        if (adapter.getItemCount() - 1 == pos) {
                            pageCount++;
                            getNews();
                        }

                    }
                }

            }
        });

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, GalleriesActivity.class);
        context.startActivity(starter);
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

        private ArrayList<Photo> items;

        public void setItems(ArrayList<Photo> in) {
            if (items == null)
                items = new ArrayList<>();
            items.addAll(in);
            notifyDataSetChanged();
        }


        void clearAdapter() {

            if (items != null)
                items.clear();

            notifyDataSetChanged();
        }

        @Override
        public ItemAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_item2, parent, false);
            return new ItemAdapter.ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemHolder holder, int position) {


            try {
                if (holder == null)
                    return;


                Photo photo = items.get(position);

                if (photo == null)
                    return;
                holder.photo = photo;

                String url = BuildConfig.IPAddress + "/" + photo.getGalleryThumbImage();
                holder.txtTitle.setText(photo.getGalleryNameFA());
                holder.txtDate.setText(photo.getPersianDate());


                new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(holder.imgAvatar).load();


            } catch (Exception ex) {
                LogWrapper.loge("ItemAdapter_onBindViewHolder_Exception: ", ex);
            }


        }

        @Override
        public int getItemCount() {

            return (items == null || items.size() == 0) ? 0 : items.size();

        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            CardView cardView;

            TextView txtTitle;
            TextView txtDate;
            TextView txtDetails;
            ImageView imgAvatar;
            Photo photo;


            public ItemHolder(View v) {
                super(v);

                cardView = v.findViewById(R.id.cardView);
                txtTitle = v.findViewById(R.id.txtTitle);
                txtDate = v.findViewById(R.id.txtDate);
                txtDetails = v.findViewById(R.id.txtDetails);
                imgAvatar = v.findViewById(R.id.imgAvatar);

                FontChanger.applyMainFont(cardView);

                int width = getResources().getDisplayMetrics().widthPixels;
                final ViewGroup.LayoutParams params = cardView.getLayoutParams();
                params.height = (int) (width * 0.6);
                cardView.setLayoutParams(params);


                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PhotoViewActivity.start(GalleriesActivity.this, photo);

                    }
                });

            }
        }
    }

    private void getNews() {
        if (photosRepo == null)
            return;

        lyBottomProgress.setVisibility(View.VISIBLE);

        photosRepo.galleries(getApplicationContext(), pageCount, user.getTokenType(), user.getToken(), new IRepoCallBack<PhotosWrapper>() {
            @Override
            public void onAnswer(PhotosWrapper answer) {
                lyProgress.setVisibility(View.GONE);
                lyBottomProgress.setVisibility(View.GONE);
                if (answer == null) {

                    return;
                }
                if (answer.getPhotos() == null || answer.getPhotos().size() == 0)
                    return;

                if (answer.getPhotos().size() < 10) {
                    hasMore = 0;

                } else {
                    hasMore = 1;
                }

                if (adapter != null)
                    adapter.setItems(answer.getPhotos());
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
