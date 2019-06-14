package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AhkamWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPayroll;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.PayrollServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;

public class AhkamHeaderActivity extends AppCompatActivity {

    private IPayroll repo;
    private LinearLayout lyProgress;


    private CardView lyPickYear;
    private LinearLayout lyAction;
    private EditText edtYear;

    private String selectedHokm;

    //تمامی فیش های دریافت شده
    private ArrayList<String> ahkam;


    private ImageView imgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahkam_header);

        initToolbar();
        initRepo();
        initViews();
        getData();
        initListeners();


    }

    private void initListeners() {
        lyPickYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYear();
            }
        });

        edtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYear();
            }
        });


        lyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(selectedHokm))
                    AhkamFooterActivity.start(AhkamHeaderActivity.this, selectedHokm);
            }
        });
    }


    private void getData() {
        if (repo == null)
            return;

        lyProgress.setVisibility(View.VISIBLE);

        repo.allAvailableAhkam(getApplicationContext(), new IRepoCallBack<AhkamWrapper>() {
            @Override
            public void onAnswer(AhkamWrapper answer) {

                lyProgress.setVisibility(View.GONE);
                if (answer == null) {
                    new CustomDialogBuilder().showAlert(AhkamHeaderActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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

                    return;
                }

                if (answer.getData() == null || answer.getData().size() == 0) {
                    new CustomDialogBuilder().showAlert(AhkamHeaderActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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
                    return;
                }

                ahkam = answer.getData();
                mapData();
            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                new CustomDialogBuilder().showAlert(AhkamHeaderActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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
                return;


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });

    }

    private void mapData() {

        if (ahkam == null || ahkam.size() == 0)
            return;

        selectedHokm = ahkam.get(ahkam.size() - 1);
        initContent();


    }

    private void initRepo() {

        if (repo == null)
            repo = new PayrollServerRepo();

    }

    private void initViews() {

        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);

        lyPickYear = (CardView) findViewById(R.id.lyPickYear);
        lyAction = (LinearLayout) findViewById(R.id.lyAction);
        edtYear = (EditText) findViewById(R.id.edtYear);
        imgBackground = (ImageView) findViewById(R.id.imgBackground);

        new ImageViewWrapper(getApplicationContext()).into(imgBackground).loadBlur(R.drawable.bg_calendar);

        try {
            FontChanger.applyMainFont(findViewById(R.id.lyRoot));
//            FontChanger.applyTitleFont(findViewById(R.id.txtDateHeader));
            FontChanger.applyTitleFont(findViewById(R.id.lyAction));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_ahkam_header), null);

    }

    private void initContent() {

        if (selectedHokm == null)
            return;

        try {

            edtYear.setText(selectedHokm);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectYear() {

        if (ahkam == null || ahkam.size() == 0)
            return;

        final ArrayList<String> availableHokms = new ArrayList<>();

        for (int i = 0; i < ahkam.size(); i++) {

            if (i == 0)
                availableHokms.add(ahkam.get(0));
            else {
                boolean available = true;
                for (int j = 0; j < availableHokms.size(); j++) {
                    if (ahkam.get(i).equals(ahkam.get(j)))
                        available = false;
                }
                if (available)
                    availableHokms.add(ahkam.get(i));
            }

        }

        CharSequence items[] = new CharSequence[availableHokms.size()];

        for (int i = 0; i < availableHokms.size(); i++) {
            items[i] = availableHokms.get(i);
        }

        try {
            new CustomDialogBuilder().showPickerDialog(AhkamHeaderActivity.this, "انتخاب سال", items, new CustomDialogBuilder.OnPickerDialogListener() {
                @Override
                public void onPick(int position) {
                    selectedHokm = availableHokms.get(position);
                    initContent();
                }
            });
        } catch (Exception ex) {
            LogWrapper.loge("ChequesActivity_onClick_Exception: ", ex);
        }

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, AhkamHeaderActivity.class);
        context.startActivity(starter);
    }

}
