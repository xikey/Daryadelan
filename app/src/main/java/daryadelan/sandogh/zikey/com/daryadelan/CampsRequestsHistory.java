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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.ICamp;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.CampServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class CampsRequestsHistory extends AppCompatActivity {


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
        setContentView(R.layout.activity_camps_requests_history);

        initRepo();
        initToolbar();
        getUserData();
        initViews();
        initRecycleView();
        getCamps();


    }

    private void getCamps() {

    }

    private void initViews() {


        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        lyBottomProgress = (LinearLayout) findViewById(R.id.lyBottomProgress);
        lyProgress.setVisibility(View.VISIBLE);

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, CampsRequestsHistory.class);
        context.startActivity(starter);
    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_camps_requests_history), null);
    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new  ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);



    }

    private void initRepo() {

        if (campRepo == null)
            campRepo = new CampServerRepo();

    }


    private void getUserData() {
        user = UserInstance.getInstance().getUser();
        if (user == null) {
            Toasty.error(CampsRequestsHistory.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }
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
                holder.txtDesc.setText(camp.getState() + "-"+camp.getCity());


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
            TextView txtDesc;
            ImageView imgAvatar;
            Camp camp;

            CardView crdConfirmCamp;


            public ItemHolder(View v) {
                super(v);


                txtTitle = v.findViewById(R.id.txtTitle);
                txtRate = v.findViewById(R.id.txtRate);
                txtDetails = v.findViewById(R.id.txtDetails);
                imgAvatar = v.findViewById(R.id.imgAvatar);
                lyRoot = v.findViewById(R.id.lyRoot);
                txtDesc = v.findViewById(R.id.txtDesc);
                crdConfirmCamp = v.findViewById(R.id.crdConfirmCamp);

                FontChanger.applyMainFont(lyRoot);
                FontChanger.applyTitleFont(txtTitle);


                lyRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CampActivity.start(CampsRequestsHistory.this, camp);
                    }
                });

                crdConfirmCamp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConfirmCampActivity.start(CampsRequestsHistory.this,camp);
                    }
                });


            }
        }
    }



}
