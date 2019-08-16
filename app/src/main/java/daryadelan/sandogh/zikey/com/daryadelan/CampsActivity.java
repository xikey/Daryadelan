package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.ICamp;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPhotos;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.CampServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.PhotosServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class CampsActivity extends AppCompatActivity {

    private RecyclerView rvItem;
    private ItemAdapter adapter;
    private LinearLayout lyProgress;
    private LinearLayout lyBottomProgress;
    private ICamp campRepo;
    private User user;
    private long pageCount = 0;
    private int hasMore = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camps);

        initRepo();
        initToolbar();
        getUserData();
        initViews();
        initRecycleView();
        getCamps();

    }

    private void initViews() {


        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        lyBottomProgress = (LinearLayout) findViewById(R.id.lyBottomProgress);
        lyProgress.setVisibility(View.VISIBLE);

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, CampsActivity.class);

        context.startActivity(starter);
    }

    private void getCamps() {
        if (campRepo == null)
            return;

        lyBottomProgress.setVisibility(View.VISIBLE);

        campRepo.allCamps(getApplicationContext(), user.getTokenType(), user.getToken(), new IRepoCallBack<CampsWrapper>() {
            @Override
            public void onAnswer(CampsWrapper answer) {
                lyProgress.setVisibility(View.GONE);
                lyBottomProgress.setVisibility(View.GONE);
                if (answer == null) {

                    return;
                }
                if (answer.getCamps() == null || answer.getCamps().size() == 0)
                    return;


                if (adapter != null)
                    adapter.setItems(answer.getCamps());
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


    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(CampsActivity.this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);

//        rvItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (hasMore == 0)
//                    return;
//
//                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    int pos = layoutManager.findLastVisibleItemPosition();
//                    if (adapter != null) {
//                        if (adapter.getItemCount() - 1 == pos) {
//                            pageCount++;
//                            getNews();
//                        }
//
//                    }
//                }
//
//            }
//        });

    }


    private void initRepo() {

        if (campRepo == null)
            campRepo = new CampServerRepo();

    }

    private void getUserData() {
        user = UserInstance.getInstance().getUser();
        if (user == null) {
            Toasty.error(CampsActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }
    }


    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_camps), null);
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

        private ArrayList<Camp> items;

        public void setItems(ArrayList<Camp> in) {
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_camps_item, parent, false);
            return new ItemAdapter.ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemHolder holder, int position) {


            try {
                if (holder == null)
                    return;


                Camp camp = items.get(position);

                if (camp == null)
                    return;
                holder.camp = camp;

                String url = BuildConfig.IPAddress + "/" + camp.getImagePath();
                holder.txtTitle.setText(camp.getCampName());
                holder.txtRate.setText(camp.getStar() + "ستاره");


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

            RelativeLayout lyRoot;

            TextView txtTitle;
            TextView txtRate;
            TextView txtDetails;
            ImageView imgAvatar;
            Camp camp;


            public ItemHolder(View v) {
                super(v);


                txtTitle = v.findViewById(R.id.txtTitle);
                txtRate = v.findViewById(R.id.txtRate);
                txtDetails = v.findViewById(R.id.txtDetails);
                imgAvatar = v.findViewById(R.id.imgAvatar);
                lyRoot = v.findViewById(R.id.lyRoot);

                FontChanger.applyMainFont(lyRoot);
                FontChanger.applyTitleFont(txtTitle);


                lyRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CampActivity.start(CampsActivity.this, camp);
                    }
                });


            }
        }
    }


}
