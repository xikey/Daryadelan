package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.News;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.INews;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.NewsServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class NewsActivity extends AppCompatActivity {

    private final String KEY_NEWS_NAME = "news";

    private RecyclerView rvItem;
    private ItemAdapter adapter;
    private LinearLayout lyProgress;
    private LinearLayout lyBottomProgress;
    private INews newsRepo;
    private User user;
    private long pageCount = 0;
    private int hasMore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

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
            Toasty.error(NewsActivity.this, "خطا در دریافت اطلاعات کاربری");
            finish();
        }
    }

    private void initRepo() {

        if (newsRepo == null)
            newsRepo = new NewsServerRepo();

    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_news), null);
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

        rvItem.setLayoutManager(new LinearLayoutManager(NewsActivity.this, LinearLayoutManager.VERTICAL, false));
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
                 if (adapter!=null){
                     if (adapter.getItemCount()-1==pos){
                         pageCount++;
                         getNews();
                     }

                 }
                }

            }
        });

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, NewsActivity.class);
        context.startActivity(starter);
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

        private ArrayList<News> items;

        public void setItems(ArrayList<News> in) {
            if (items==null)
                items=new ArrayList<>();
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_item, parent, false);
            return new ItemAdapter.ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemHolder holder, int position) {


            try {
                if (holder == null)
                    return;


                News news = items.get(position);
                if (news == null)
                    return;

                holder.news = news;
                String url = BuildConfig.IPAddress + "/" + news.getPostThumbImage();
                holder.txtTitle.setText(news.getPostTitle());
                holder.txtDate.setText(news.getCreateDate());
                holder.txtDetails.setText(news.getPostBody());
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
            News news;


            public ItemHolder(View v) {
                super(v);

                cardView = v.findViewById(R.id.cardView);
                txtTitle = v.findViewById(R.id.txtTitle);
                txtDate = v.findViewById(R.id.txtDate);
                txtDetails = v.findViewById(R.id.txtDetails);
                imgAvatar = v.findViewById(R.id.imgAvatar);

                FontChanger.applyMainFont(cardView);


                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (news != null)
                            OpenedNewsActivity.start(NewsActivity.this, news);
                    }
                });

            }
        }
    }

    private void getNews() {
        if (newsRepo == null)
            return;

        lyBottomProgress.setVisibility(View.VISIBLE);
        newsRepo.news(getApplicationContext(), KEY_NEWS_NAME, pageCount, user.getTokenType(), user.getToken(), new IRepoCallBack<NewsWrapper>() {
            @Override
            public void onAnswer(NewsWrapper answer) {
                lyProgress.setVisibility(View.GONE);
                lyBottomProgress.setVisibility(View.GONE);
                if (answer == null) {

                    return;
                }
                if (answer.getNews() == null || answer.getNews().size() == 0)
                    return;

                if (answer.getNews().size()<10){
                    hasMore=0;

                }else {
                    hasMore=1;
                }

                if (adapter != null)
                    adapter.setItems(answer.getNews());
            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                lyBottomProgress.setVisibility(View.GONE);
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
