package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.os.Build;
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
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.CampRequestHistory;
import daryadelan.sandogh.zikey.com.daryadelan.model.CampReseption;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.ICamp;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.CampServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import es.dmoral.toasty.Toasty;

public class ConfirmedCampHistoryActivity extends AppCompatActivity {

    private static final String KEY_PARCABLE = "PARCABLE";

    private CampRequestHistory campRequestHistory;
    private LinearLayout lyAddPerson;
    private LinearLayout lyDate;
    private LinearLayout lyDayCount;
    private RelativeLayout lyAddPersonFloat;

    private TextView txtDate;
    private TextView txtDayCount;


    private ImageView imgDropDown;
    private ImageView imgFullPhoto;

    private TextView txtCampName;
    private TextView txtCampDetail;
    private TextView txtPersonsCount;

    private RelativeLayout lyFullPhoto;
    private CardView crdAddNewPerson;

    private RecyclerView rvItem;
    private ItemAdapter adapter;

    private LinearLayout lyAction;
    private LinearLayout lyConstLayouts;

    private ArrayList<CampReseption> campReseptions;

    private ICamp campRepo;

    private User user;
    private LinearLayout lyProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }

        setContentView(R.layout.activity_confirmed_camp_history);


        parseIntent();
        initRepo();
        getUserData();
        initViews();
        initClickListeners();
        initContent();
        initRecycleView();

    }

    private void getUserData() {

        user = UserInstance.getInstance().getUser();
        if (user == null) {
            Toasty.error(ConfirmedCampHistoryActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }
    }

    private void initRepo() {
        if (campRepo == null)
            campRepo = new CampServerRepo();
    }


    private void initContent() {

        Camp camp = campRequestHistory.getCamp();
        if (camp == null)
            return;


        if (!TextUtils.isEmpty(campRequestHistory.getCreateDate()))
            txtDate.setText(campRequestHistory.getRequestDate());

        if (campReseptions == null) {
            txtPersonsCount.setText("0 نفر");
        } else {
            txtPersonsCount.setText(campReseptions.size() + " نفر ");
        }

        txtDayCount.setText(campRequestHistory.getCount() + " روز ");
        txtCampName.setText(camp.getCampName());
        txtCampDetail.setText(camp.getState() + " - " + camp.getCity() + " - " + camp.getStar() + " ستاره");

        if (imgFullPhoto != null) {
            String url = BuildConfig.IPAddress + "/" + camp.getImagePath();
            new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(imgFullPhoto).load();
        }


    }


    private void initClickListeners() {



        lyAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideList();
            }
        });

        imgDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideList();
            }
        });


    }


    private void initViews() {

        lyAddPerson = (LinearLayout) findViewById(R.id.lyAddPerson);
        lyDate = (LinearLayout) findViewById(R.id.lyDate);
        lyDayCount = (LinearLayout) findViewById(R.id.lyDayCount);

        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDayCount = (TextView) findViewById(R.id.txtDayCount);
        txtPersonsCount = (TextView) findViewById(R.id.txtPersonsCount);
        lyAddPersonFloat = (RelativeLayout) findViewById(R.id.lyAddPersonFloat);
        imgDropDown = (ImageView) findViewById(R.id.imgDropDown);
        imgFullPhoto = (ImageView) findViewById(R.id.imgFullPhoto);

        txtCampName = (TextView) findViewById(R.id.txtCampName);
        txtCampDetail = (TextView) findViewById(R.id.txtCampDetail);

        lyFullPhoto = (RelativeLayout) findViewById(R.id.lyFullPhoto);

        crdAddNewPerson = (CardView) findViewById(R.id.crdAddNewPerson);

        lyAction = (LinearLayout) findViewById(R.id.lyAction);
        lyConstLayouts = (LinearLayout) findViewById(R.id.lyConstLayouts);

        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);


        try {
            FontChanger.applyMainFont(findViewById(R.id.lyContent));

            int width = getResources().getDisplayMetrics().widthPixels;

            final ViewGroup.LayoutParams params = lyFullPhoto.getLayoutParams();
            params.height = (width * 2 / 3);
            lyFullPhoto.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(ConfirmedCampHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);


    }

    private void parseIntent() {

        Intent data = getIntent();
        if (data == null)
            return;



    }


    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, ConfirmedCampHistoryActivity.class);
        context.startActivity(starter);
    }

    private void showHideList() {

        if (lyAddPersonFloat == null)
            return;
        if (lyAddPersonFloat.getVisibility() == View.VISIBLE) {
            slideDown(lyAddPersonFloat);

        } else {
            slideUp(lyAddPersonFloat);
        }


    }


    public void slideUp(View view) {

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                lyConstLayouts.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setInterpolator(new DecelerateInterpolator());
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lyAction.setVisibility(View.GONE);
                lyAddPerson.setVisibility(View.GONE);
                lyDayCount.setVisibility(View.GONE);
                lyDate.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
        lyAddPersonFloat.setVisibility(View.VISIBLE);
        imgDropDown.setVisibility(View.VISIBLE);
        crdAddNewPerson.setVisibility(View.VISIBLE);
        rvItem.setVisibility(View.VISIBLE);


    }

    public void slideDown(final View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                lyConstLayouts.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setInterpolator(new AccelerateInterpolator());
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lyAddPersonFloat.setVisibility(View.GONE);
                imgDropDown.setVisibility(View.GONE);
                crdAddNewPerson.setVisibility(View.GONE);
                rvItem.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
        lyAction.setVisibility(View.VISIBLE);
        lyAddPerson.setVisibility(View.VISIBLE);
        lyDayCount.setVisibility(View.VISIBLE);
        lyDate.setVisibility(View.VISIBLE);
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {


        @Override
        public ItemAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_person_for_camp_item, parent, false);
            return new ItemAdapter.ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemHolder holder, int position) {


            try {
                if (holder == null)
                    return;

                holder.campReseption = campReseptions.get(position);
                CampReseption cmp = campReseptions.get(position);
                holder.pos = position;

                holder.edtName.setText(cmp.getName());
                holder.edtFamily.setText(cmp.getFamily());
                holder.edtNationalCode.setText("" + (int) cmp.getNationalCode());
                holder.edtRelation.setText("" + cmp.getRelation());
            } catch (Exception ex) {
                LogWrapper.loge("ItemAdapter_onBindViewHolder_Exception: ", ex);
            }


        }

        @Override
        public int getItemCount() {

            return (campReseptions != null && campReseptions.size() != 0) ? campReseptions.size() : 0;

        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            CardView lyRoot;
            CampReseption campReseption;
            int pos;
            TextView edtName;
            TextView edtFamily;
            TextView edtNationalCode;
            TextView edtRelation;

            public ItemHolder(View v) {
                super(v);

                lyRoot = v.findViewById(R.id.lyRoot);

                edtName = v.findViewById(R.id.edtName);
                edtFamily = v.findViewById(R.id.edtFamily);
                edtNationalCode = v.findViewById(R.id.edtNationalCode);
                edtRelation = v.findViewById(R.id.edtRelation);

                FontChanger.applyMainFont(lyRoot);

                lyRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CampReseptionFragment.Show_EditableMode(ConfirmedCampHistoryActivity.this, new CampReseptionFragment.ISaveForm() {
                            @Override
                            public void onSaveForm(CampReseption campReseption) {

                            }

                            @Override
                            public void onEdit(CampReseption campReseption, int pos) {
                                campReseptions.set(pos, campReseption);
                                adapter.notifyDataSetChanged();
                                initContent();
                            }

                            @Override
                            public void onRemove(int pos) {
                                campReseptions.remove(pos);
                                adapter.notifyDataSetChanged();
                                initContent();
                            }
                        }, campReseption, pos);
                    }
                });


            }
        }
    }



}
