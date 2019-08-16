package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;

import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CalendarWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.PersianDateConverter;
import es.dmoral.toasty.Toasty;

public class ConfirmCampActivity extends AppCompatActivity {

    private static final String KEY_PARCABLE = "PARCABLE";

    private Camp camp;
    private LinearLayout lyAddPerson;
    private LinearLayout lyDate;
    private LinearLayout lyDayCount;

    private TextView txtDate;
    private TextView txtDayCount;

    private String date = CalendarWrapper.getCurrentPersianDate();

    private int dayCount = 1;

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

        txtDayCount.setText(dayCount+" روز ");

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
    }

    private void initViews() {

        lyAddPerson = (LinearLayout) findViewById(R.id.lyAddPerson);
        lyDate = (LinearLayout) findViewById(R.id.lyDate);
        lyDayCount = (LinearLayout) findViewById(R.id.lyDayCount);

        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDayCount = (TextView) findViewById(R.id.txtDayCount);

        try {
            FontChanger.applyMainFont(findViewById(R.id.lyContent));
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


}
