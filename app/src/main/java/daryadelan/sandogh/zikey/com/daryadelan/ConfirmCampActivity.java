package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;

import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CalendarWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
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

    private String date = CalendarWrapper.getCurrentPersianDate();

    private int dayCount = 1;

    private ImageView imgDropDown;
    private ImageView imgFullPhoto;

    private TextView txtCampName;
    private TextView txtCampDetail;

    private RelativeLayout lyFullPhoto;
    private CardView crdAddNewPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_camp);

        parseIntent();
        initViews();
        initClickListeners();
        initContent();


    }

    private void initContent() {

        if (!TextUtils.isEmpty(date))
            txtDate.setText(date);

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
                new CustomDialogBuilder().showDatePicker(ConfirmCampActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                        date = PersianDateConverter.toPersianFormat(i, i1, i2);
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

                        dayCount = Integer.parseInt(input);
                        initContent();

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

            }
        });
    }

    private void initViews() {

        lyAddPerson = (LinearLayout) findViewById(R.id.lyAddPerson);
        lyDate = (LinearLayout) findViewById(R.id.lyDate);
        lyDayCount = (LinearLayout) findViewById(R.id.lyDayCount);

        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDayCount = (TextView) findViewById(R.id.txtDayCount);
        lyAddPersonFloat = (RelativeLayout) findViewById(R.id.lyAddPersonFloat);
        imgDropDown = (ImageView) findViewById(R.id.imgDropDown);
        imgFullPhoto = (ImageView) findViewById(R.id.imgFullPhoto);

        txtCampName = (TextView) findViewById(R.id.txtCampName);
        txtCampDetail = (TextView) findViewById(R.id.txtCampDetail);

        lyFullPhoto = (RelativeLayout) findViewById(R.id.lyFullPhoto);

        crdAddNewPerson = (CardView) findViewById(R.id.crdAddNewPerson);

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
                lyAddPersonFloat.getHeight(),  // fromYDelta
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

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
        lyAddPersonFloat.setVisibility(View.VISIBLE);
        imgDropDown.setVisibility(View.VISIBLE);

        lyAddPerson.setVisibility(View.GONE);
        lyDayCount.setVisibility(View.GONE);
        lyDate.setVisibility(View.GONE);
    }

    // slide the view from its current position to below itself
    public void slideDown(final View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                lyAddPersonFloat.getHeight()); // toYDelta
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
                imgDropDown.setVisibility(View.VISIBLE);

                lyAddPerson.setVisibility(View.VISIBLE);
                lyDayCount.setVisibility(View.VISIBLE);
                lyDate.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);

    }

}
