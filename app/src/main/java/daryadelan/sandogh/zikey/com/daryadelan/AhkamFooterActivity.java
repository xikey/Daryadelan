package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.Payroll;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.HokmWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPayroll;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.PayrollServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import de.hdodenhof.circleimageview.CircleImageView;

public class AhkamFooterActivity extends AppCompatActivity {

    private static final String KEY_YEAR = "YEAR";
    private static final String KEY_PERSONEL_CODE = "PERSONEL_CODE";
    private String year;
    private IPayroll repo;

    private LinearLayout lyProgress;

    private RecyclerView rvItem;
    private ItemAdapter adapter;

    public SubsamplingScaleImageView scaleImageView;

    private long personelCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahkam_footer);

        parseIntent();
        initViews();
        initRecycleView();
        initToolbar();
        initRepo();
        getData();

    }

    public static void start(FragmentActivity context, String year,long personelCode) {
        Intent starter = new Intent(context, AhkamFooterActivity.class);
        starter.putExtra(KEY_YEAR, year);
        starter.putExtra(KEY_PERSONEL_CODE, personelCode);
        context.startActivity(starter);
    }

    private void initToolbar() {
        String title = "تاریخ:  " + year;
        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, title, null);

    }


    private void initViews() {

        rvItem = (RecyclerView) findViewById(R.id.rtItem);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        scaleImageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);

        try {
            FontChanger.applyYekanFont(findViewById(R.id.lyHeader));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initRepo() {
        if (repo == null)
            repo = new PayrollServerRepo();
    }


    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvItem.setAdapter(adapter);
    }


    private void parseIntent() {

        Intent data = getIntent();

        if (data == null)
            return;

        if (data.hasExtra(KEY_YEAR))
            year = data.getStringExtra(KEY_YEAR);

        if (TextUtils.isEmpty(year))
            finish();


        if (data.hasExtra(KEY_PERSONEL_CODE))
            personelCode = data.getLongExtra(KEY_PERSONEL_CODE, 0);

    }

    private void getData() {

        if (repo == null)
            return;

        lyProgress.setVisibility(View.VISIBLE);

        repo.getHokm(getApplicationContext(), year,personelCode, new IRepoCallBack<HokmWrapper>() {
            @Override
            public void onAnswer(HokmWrapper answer) {

                lyProgress.setVisibility(View.GONE);
                if (answer == null) {
                    new CustomDialogBuilder().showAlert(AhkamFooterActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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
                    new CustomDialogBuilder().showAlert(AhkamFooterActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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
                Base64Decode(answer.getData().get(0).getByteData(), scaleImageView);
                adapter.setPayrolls(answer.getData());

            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                new CustomDialogBuilder().showAlert(AhkamFooterActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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

    private void Base64Decode(String base64, ImageView imageView) {
        if (imageView == null)
            return;

        if (TextUtils.isEmpty(base64))
            return;

        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);


    }
 private void Base64Decode(String base64, SubsamplingScaleImageView imageView) {
        if (imageView == null)
            return;

        if (TextUtils.isEmpty(base64))
            return;

        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImage(ImageSource.bitmap(decodedByte));


    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {


        private ArrayList<Payroll> payrolls;

        public ArrayList<Payroll> getPayrolls() {
            return payrolls;
        }

        public void setPayrolls(ArrayList<Payroll> payrolls) {
            this.payrolls = payrolls;
            notifyDataSetChanged();
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hokm_item, parent, false);
            FontChanger.applyMainFont(view);
            return new ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {

            if (holder == null)
                return;

            Payroll payroll = payrolls.get(position);
            holder.payroll = payroll;

            try {

                Base64Decode(payroll.getByteData(), holder.imgHokm);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {

            return (payrolls == null || payrolls.size() == 0) ? 0 : payrolls.size();

        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            CircleImageView imgHokm;
            Payroll payroll;

            public ItemHolder(View v) {
                super(v);

                imgHokm = v.findViewById(R.id.imgHokm);

                imgHokm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (payroll==null)
                            return;

                        Base64Decode(payroll.getByteData(), scaleImageView);

                    }
                });

            }
        }
    }


}
