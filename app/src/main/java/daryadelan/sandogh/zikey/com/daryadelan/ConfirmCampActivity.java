package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
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

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Calendar;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.CampReseption;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampReseptionRequesWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.ICamp;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.CampServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.PersianDateConverter;
import es.dmoral.toasty.Toasty;

public class ConfirmCampActivity extends AppCompatActivity {

    private static final String KEY_PARCABLE = "PARCABLE";

    private Camp camp;
    private LinearLayout lyAddPerson;
    private LinearLayout lyDate;
    private LinearLayout lyDayCount;
    private RelativeLayout lyAddPersonFloat;

    private TextView txtDate;
    private TextView txtDayCount;

    private String date = "";
    private String originalDate = "";

    private int dayCount = 0;

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


        setContentView(R.layout.activity_confirm_camp);

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
            Toasty.error(ConfirmCampActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }
    }


    private void initRepo() {
        if (campRepo == null)
            campRepo = new CampServerRepo();
    }

    private void initContent() {

        if (!TextUtils.isEmpty(date))
            txtDate.setText(date);
        else {
            txtDate.setText("انتخاب تاریخ");
        }


        if (campReseptions == null) {
            txtPersonsCount.setText("0 نفر");
        } else {
            txtPersonsCount.setText(campReseptions.size() + " نفر ");
        }

        txtDayCount.setText(dayCount + " روز ");

        txtCampName.setText(camp.getCampName());
        txtCampDetail.setText(camp.getState() + " - " + camp.getCity() + " - " + camp.getStar() + " ستاره");

        if (imgFullPhoto != null) {
            String url = BuildConfig.IPAddress + "/" + camp.getImagePath();
            new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(imgFullPhoto).load();
        }


    }

    private void initClickListeners() {


        lyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomDialogBuilder().showDatePicker(ConfirmCampActivity.this, new CustomDialogBuilder.OnDatePickerListener() {
                    @Override
                    public void onDateSet(PersianCalendar calendar, int year, int month, int day) {
                        date = PersianDateConverter.toPersianFormat(year, month, day);

                        calendar.setPersianDate(year, month, day);

                        Calendar orgCalendar = Calendar.getInstance();
                        orgCalendar.setTimeInMillis(calendar.getTimeInMillis());

                        int yr = (orgCalendar.get(Calendar.YEAR));
                        int mn = (orgCalendar.get(Calendar.MARCH));
                        int dy = (orgCalendar.get(Calendar.DATE));

                        originalDate = PersianDateConverter.toPersianFormat(yr, mn, dy);

                        Toasty.info(ConfirmCampActivity.this, "" + originalDate).show();

                        initContent();
                    }
                });
            }
        });


        lyDayCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomDialogBuilder().showInputTextDialog_NumbersOnly(ConfirmCampActivity.this, "تعداد روز", new CustomDialogBuilder.OnDialogListener() {
                    @Override
                    public void onOK(String input) {
                        if (!TextUtils.isEmpty(input)) {
                            dayCount = Integer.parseInt(input);
                            initContent();
                        }

                    }

                    @Override
                    public void onNeutral(String input) {

                    }
                });
            }
        });

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

        crdAddNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CampReseptionFragment.Show(ConfirmCampActivity.this, new CampReseptionFragment.ISaveForm() {
                    @Override
                    public void onSaveForm(CampReseption campReseption) {
                        if (campReseptions == null)
                            campReseptions = new ArrayList<>();

                        campReseptions.add(campReseption);
                        adapter.notifyDataSetChanged();
                        initContent();
                    }

                    @Override
                    public void onEdit(CampReseption campReseption, int pos) {

                    }

                    @Override
                    public void onRemove(int pos) {

                    }
                });

            }
        });


        lyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (camp == null) {
                    new CustomDialogBuilder().showAlert(ConfirmCampActivity.this, "اطلاعات ورودی کامل نمیباشد");
                    return;
                }

                if (campReseptions == null || campReseptions.size() == 0) {
                    new CustomDialogBuilder().showAlert(ConfirmCampActivity.this, "لیست افراد خالی میباشد");
                    return;
                }

                if (TextUtils.isEmpty(originalDate)) {
                    new CustomDialogBuilder().showAlert(ConfirmCampActivity.this, "تاریخ درخواست خالی میباشد");
                    return;
                }

                if (dayCount == 0) {
                    new CustomDialogBuilder().showAlert(ConfirmCampActivity.this, "تعداد روز مشخص نمیباشد");
                    return;
                }

                camp.setCampReseptions(campReseptions);
                camp.setDay(dayCount);
                camp.setCount(campReseptions.size());
                camp.setRequestDate(originalDate);

                sendData();
            }
        });


    }

    private void sendData() {
        lyProgress.setVisibility(View.VISIBLE);

        campRepo.requestCamp(getApplicationContext(), camp, user.getTokenType(), user.getToken(), user, new IRepoCallBack<CampReseptionRequesWrapper>() {
            @Override
            public void onAnswer(CampReseptionRequesWrapper answer) {

                lyProgress.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(answer.getData())) {
                    new CustomDialogBuilder().showAlert(ConfirmCampActivity.this, "درخواست شما با موفقیت ثبت گردید", new CustomAlertDialog.OnCancelClickListener() {
                        @Override
                        public void onClickCancel(DialogFragment fragment) {
                            fragment.dismiss();
                            finish();
                        }

                        @Override
                        public void onClickOutside(DialogFragment fragment) {
                            fragment.dismiss();
                            finish();
                        }
                    });

                } else {
                    new CustomDialogBuilder().showAlert(ConfirmCampActivity.this, "خطا در ثبت اطلاعات");
                }

            }

            @Override
            public void onError(Throwable error) {
                new CustomDialogBuilder().showAlert(ConfirmCampActivity.this, error.getMessage());
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

        rvItem.setLayoutManager(new LinearLayoutManager(ConfirmCampActivity.this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);


    }


    private void parseIntent() {

        Intent data = getIntent();
        if (data == null)
            return;

        if (data.hasExtra(KEY_PARCABLE))
            camp = data.getExtras().getParcelable(KEY_PARCABLE);

        if (camp == null) {
            Toasty.error(this, "خطا در دریافت اطلاعات  ").show();

        }

    }

    public static void start(FragmentActivity context, Camp camp) {
        Intent starter = new Intent(context, ConfirmCampActivity.class);
        starter.putExtra(KEY_PARCABLE, camp);
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

    // slide the view from below itself to the current position
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

    // slide the view from its current position to below itself
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
                holder.edtRelation.setText("" + cmp.getRelationShipName());
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
                        CampReseptionFragment.Show_EditableMode(ConfirmCampActivity.this, new CampReseptionFragment.ISaveForm() {
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
