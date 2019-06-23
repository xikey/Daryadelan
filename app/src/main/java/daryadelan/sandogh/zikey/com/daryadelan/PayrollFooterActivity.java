package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.Payroll;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PayrollWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPayroll;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.PayrollServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.NumberSeperator;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;

public class PayrollFooterActivity extends AppCompatActivity {

    private static final String KEY_YEAR = "YEAR";
    private static final String KEY_MONTH = "MONTH";

    private long year;
    private long month;

    private RecyclerView rvItem;
    private ItemAdapter adapter;

    private LinearLayout lyProgress;

    private IPayroll repo;

    private AppBarLayout AppBarLayout;

    private Payroll payroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll_footer);

        parseIntent();
        initToolbar();
        initRepo();
        initViews();
        initRecycleView();
        getData();


    }

    private void initRepo() {
        if (repo == null)
            repo = new PayrollServerRepo();
    }

    private void initViews() {

        rvItem = (RecyclerView) findViewById(R.id.rtItem);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        AppBarLayout = (android.support.design.widget.AppBarLayout) findViewById(R.id.appbar);
        AppBarLayout.setExpanded(true);

        try {
            FontChanger.applyYekanFont(findViewById(R.id.lyHeader));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);
    }

    private void getData() {

        if (repo == null)
            return;
        lyProgress.setVisibility(View.VISIBLE);

        repo.getPayroll(getApplicationContext(), year, month, new IRepoCallBack<PayrollWrapper>() {
            @Override
            public void onAnswer(PayrollWrapper answer) {
                lyProgress.setVisibility(View.GONE);
                if (answer == null) {
                    new CustomDialogBuilder().showAlert(PayrollFooterActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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
                    new CustomDialogBuilder().showAlert(PayrollFooterActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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

                MappingDate(answer.getData());
            }

            @Override
            public void onError(Throwable error) {

                lyProgress.setVisibility(View.GONE);
                new CustomDialogBuilder().showAlert(PayrollFooterActivity.this, getString(R.string.error_getting_data_please_try_again), new CustomAlertDialog.OnCancelClickListener() {
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

    private void initToolbar() {
        String title = "فیش حقوقی:  " +  payroll.getMonthAsString()+ " "+year;
        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, title, null);

    }

    private void parseIntent() {

        Intent data = getIntent();

        if (data == null)
            return;

        if (data.hasExtra(KEY_MONTH))
            month = data.getLongExtra(KEY_MONTH, 0);

        if (data.hasExtra(KEY_YEAR))
            year = data.getLongExtra(KEY_YEAR, 0);

        if (month == 0 || year == 0)
            finish();


        if (payroll==null)
        {
            payroll=new Payroll();
            payroll.setYear(String.valueOf(year));
            payroll.setMonth(String.valueOf(month));

        }

    }

    public static void start(FragmentActivity context, long year, long month) {

        Intent starter = new Intent(context, PayrollFooterActivity.class);
        starter.putExtra(KEY_YEAR, year);
        starter.putExtra(KEY_MONTH, month);
        context.startActivity(starter);
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payrolls_item_grid, parent, false);
            FontChanger.applyMainFont(view);
            return new ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {

            if (holder == null)
                return;

            try {
                Payroll payroll = payrolls.get(position);

                holder.txtTitle.setText(payroll.getDesc());

                holder.txtPrice.setText("0");
                holder.txtNavigatePrice.setText("0");
                holder.txtNavigatePrice.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.txtPrice.setBackgroundColor(Color.parseColor("#ffffff"));

                if (payroll.getTransactionType() == 1) {

                    holder.txtPrice.setBackgroundColor(Color.parseColor("#F1F8E9"));
                    holder.txtPrice.setText(NumberSeperator.separate(payroll.getAmount()));


                }
                if (payroll.getTransactionType() == -1) {

                    holder.txtNavigatePrice.setBackgroundColor(Color.parseColor("#ffebee"));
                    holder.txtNavigatePrice.setText(NumberSeperator.separate(payroll.getAmount()));
                }

                if (payroll.getTransactionType() == -10) {

                    holder.txtPrice.setBackgroundColor(Color.parseColor("#DCEDC8"));
                    holder.txtPrice.setText(NumberSeperator.separate(payroll.getAmount()));
                }
                if (payroll.getTransactionType() == -11) {

                    holder.txtNavigatePrice.setBackgroundColor(Color.parseColor("#FFCCBC"));
                    holder.txtNavigatePrice.setText(NumberSeperator.separate(payroll.getAmount()));
                }
                if (payroll.getTransactionType() == -12) {

                    if (payroll.getAmount()<0){
                        holder.txtNavigatePrice.setBackgroundColor(Color.parseColor("#FFE082"));
                        holder.txtNavigatePrice.setText(NumberSeperator.separate(payroll.getAmount()));
                    }
                    else {
                        holder.txtPrice.setBackgroundColor(Color.parseColor("#FFE082"));
                        holder.txtPrice.setText(NumberSeperator.separate(payroll.getAmount()));
                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {

            return (payrolls == null || payrolls.size() == 0) ? 0 : payrolls.size();

        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            CardView cardView;
            TextView txtPrice;
            TextView txtTitle;
            TextView txtNavigatePrice;


            public ItemHolder(View v) {
                super(v);

                cardView = (CardView) v.findViewById(R.id.cardView);

                cardView = v.findViewById(R.id.cardView);
                txtPrice = v.findViewById(R.id.txtPrice);
                txtTitle = v.findViewById(R.id.txtTitle);
                txtNavigatePrice = v.findViewById(R.id.txtNavigatePrice);

                FontChanger.applyMainFont(cardView);


            }
        }
    }


    private void MappingDate(ArrayList<Payroll> inputs) {

        ArrayList<Payroll> payrolls = new ArrayList<>();
        ArrayList<Payroll> negativePayroll = new ArrayList<>();

        Payroll nep = new Payroll();
        nep.setTransactionType(-11);
        nep.setDesc("جمع کسورات");

        Payroll pop = new Payroll();
        pop.setTransactionType(-10);
        pop.setDesc("جمع واریزی");


        Payroll top = new Payroll();
        top.setTransactionType(-12);
        top.setDesc("خالص پرداختی");


        if (inputs == null || inputs.size() == 0)
            return;

        for (int i = 0; i < inputs.size(); i++) {

            Payroll p = inputs.get(i);
            if (p.getTransactionType() == 1) {
                payrolls.add(p);
                pop.setAmount(pop.getAmount() + p.getAmount());
            } else {
                negativePayroll.add(p);
                nep.setAmount(nep.getAmount() + p.getAmount());
            }

        }

        payrolls.add(pop);
        if (negativePayroll.size() != 0)
            negativePayroll.add(nep);


        ArrayList<Payroll> total = new ArrayList<>();
        total.addAll(payrolls);
        total.addAll(negativePayroll);
        top.setAmount(pop.getAmount() - nep.getAmount());
        total.add(top);


        adapter.setPayrolls(total);

    }


}
