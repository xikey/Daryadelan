package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendarUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.CampRequest;
import daryadelan.sandogh.zikey.com.daryadelan.model.CampRequestHistory;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsHistoryWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.ICamp;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.CampServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.PersianDateConverter;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class CampsRequestsHistoryActivity extends AppCompatActivity {


    private RecyclerView rvItem;
    private ItemAdapter adapter;
    private LinearLayout lyProgress;
    private LinearLayout lyBottomProgress;
    private ICamp campRepo;
    private User user;
    private int pageCount = 0;
    private int hasMore = 0;
    private CardView lyAction;


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
        initClickListeners();


    }

    private void initClickListeners() {
        if (lyAction != null)
            lyAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CampsActivity.start(CampsRequestsHistoryActivity.this);
                }
            });
    }

    private void getCamps() {
        if (campRepo == null)
            return;

        lyBottomProgress.setVisibility(View.VISIBLE);
        campRepo.campRequestsHistory(getApplicationContext(), pageCount, user.getTokenType(), user.getToken(), new IRepoCallBack<CampsHistoryWrapper>() {
            @Override
            public void onAnswer(CampsHistoryWrapper answer) {
                lyProgress.setVisibility(View.GONE);
                lyBottomProgress.setVisibility(View.GONE);
                if (answer == null) {

                    return;
                }
                if (answer.getCampRequests() == null || answer.getCampRequests().size() == 0)
                    return;

                if (answer.getCampRequests().size() < 10) {
                    hasMore = 0;

                } else {
                    hasMore = 1;
                }

                if (adapter != null) {

                }
                adapter.setItems(answer.getCampRequests());
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


    private void initViews() {


        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        lyBottomProgress = (LinearLayout) findViewById(R.id.lyBottomProgress);
        lyAction = (CardView) findViewById(R.id.lyAction);
        lyProgress.setVisibility(View.VISIBLE);

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, CampsRequestsHistoryActivity.class);
        context.startActivity(starter);
    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_camps_requests_history), null);
    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
                            getCamps();
                        }

                    }
                }

            }
        });


    }

    private void initRepo() {

        if (campRepo == null)
            campRepo = new CampServerRepo();

    }


    private void getUserData() {

        user = UserInstance.getInstance().getUser();
        if (user == null) {
            Toasty.error(CampsRequestsHistoryActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

        private ArrayList<CampRequestHistory> items;

        public void setItems(ArrayList<CampRequestHistory> in) {
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_camps_requests_history_item, parent, false);
            return new ItemAdapter.ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemHolder holder, int position) {


            try {
                if (holder == null)
                    return;


                CampRequestHistory campRequest = items.get(position);

                if (campRequest == null)
                    return;
                holder.campRequest = campRequest;

                if (campRequest.getCamp() != null) {

                    Camp camp = campRequest.getCamp();

                    String url = BuildConfig.IPAddress + "/" + camp.getImagePath();
                    holder.txtTitle.setText(camp.getCampName());
                    holder.txtRate.setText(camp.getStar() + "ستاره");
                    holder.txtDesc.setText(camp.getState() + "-" + camp.getCity());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(campRequest.getCreateDate());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    String yr = String.valueOf(cal.get(Calendar.YEAR));
                    String mn = String.valueOf(cal.get(Calendar.MARCH));
                    String dy = String.valueOf(cal.get(Calendar.DATE));
                    long milis = cal.getTimeInMillis();
                    PersianCalendar pc=new PersianCalendar();
                    pc.setTimeInMillis(milis);
                    String p= PersianDateConverter.toPersianFormat(pc.getPersianYear(),pc.getPersianMonth(),pc.getPersianDay());


                    if (date != null) {
                        holder.txtDate.setText(p);
                    }


                    if (campRequest.getCampReseptions() != null && campRequest.getCampReseptions().size() != 0)
                        holder.txtCount.setText(campRequest.getCampReseptions().size() + " نفر ");
                    new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(holder.imgAvatar).load();

                }


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
            TextView txtDate;
            TextView txtRequestDate;
            TextView txtCount;
            ImageView imgAvatar;
            CampRequestHistory campRequest;
            CardView crdShowDetails;


            public ItemHolder(View v) {
                super(v);

                txtTitle = v.findViewById(R.id.txtTitle);
                txtRate = v.findViewById(R.id.txtRate);
                txtDetails = v.findViewById(R.id.txtDetails);
                imgAvatar = v.findViewById(R.id.imgAvatar);
                lyRoot = v.findViewById(R.id.lyRoot);
                txtDate = v.findViewById(R.id.txtDate);
                txtRequestDate = v.findViewById(R.id.txtRequestDate);
                txtCount = v.findViewById(R.id.txtCount);
                crdShowDetails = v.findViewById(R.id.crdShowDetails);

                txtDesc = v.findViewById(R.id.txtDesc);

                FontChanger.applyMainFont(lyRoot);
                FontChanger.applyTitleFont(txtTitle);


                crdShowDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String json = campRequest.toJson();
                        if (!TextUtils.isEmpty(json))
                            ConfirmedCampHistoryActivity.start(CampsRequestsHistoryActivity.this, json);

                    }
                });


            }
        }
    }


}
