package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Conversation;
import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IConversation;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.ConversationServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class ConversationFooterActivity extends AppCompatActivity {

    private static final String KEY_HEADER_ID = "HEADER_ID";

    private static int TYPE_SEND = 1;
    private static int TYPE_RECEIVE = 2;

    private RecyclerView rvItem;
    private ItemAdapter adapter;
    private LinearLayout lyProgress;
    private LinearLayout lyBottomProgress;
    private IConversation conversationRepo;
    private User user;
    private int page = 0;
    private int hasMore = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_footer);

        initRepo();
        initToolbar();
        getUserData();
        initViews();
        initRecycleView();
        getData();
        initListeners();

    }

    public static void start(Context context, long messageID) {
        Intent starter = new Intent(context, ConversationFooterActivity.class);
        starter.putExtra(KEY_HEADER_ID, messageID);
        context.startActivity(starter);
    }

    private void initListeners() {

    }

    private void getData() {

        if (user == null) {
            Toasty.error(ConversationFooterActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }

        if (conversationRepo == null)
            return;

        if (page == 0)
            lyProgress.setVisibility(View.VISIBLE);

        lyBottomProgress.setVisibility(View.VISIBLE);

        conversationRepo.getAllConversationsTopics(getApplicationContext(), user.getTokenType(), user.getToken(), page, new IRepoCallBack<ConversationWrapper>() {
            @Override
            public void onAnswer(ConversationWrapper answer) {

                lyBottomProgress.setVisibility(View.GONE);

                lyProgress.setVisibility(View.GONE);
                if (answer != null && answer.getData() != null && answer.getData().size() != 0) {

                    if (answer.getData().size() < 10) {
                        hasMore = 0;

                    } else {
                        hasMore = 1;
                    }

                    adapter.setItems(answer.getData());


                } else {
                    if (page == 0)
                        Toasty.info(ConversationFooterActivity.this, "گفتگویی ایجاد نشده است، جهت ثبت گفتگوی جدید دکمه زیر صفحه را لمس نمایید").show();
                }
            }

            @Override
            public void onError(Throwable error) {
                lyBottomProgress.setVisibility(View.GONE);
                lyProgress.setVisibility(View.GONE);
                if (page == 0)
                    Toasty.info(ConversationFooterActivity.this, "گفتگویی ایجاد نشده است، جهت ثبت گفتگوی جدید دکمه زیر صفحه را لمس نمایید").show();
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
        lyProgress.setVisibility(View.VISIBLE);


    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(ConversationFooterActivity.this, LinearLayoutManager.VERTICAL, false));
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
                            page++;
                            getData();
                        }

                    }
                }

            }
        });


    }

    private void initRepo() {

        if (conversationRepo == null)
            conversationRepo = new ConversationServerRepo();

    }


    private void getUserData() {
        user = UserInstance.getInstance().getUser();
        if (user == null) {
            Toasty.error(ConversationFooterActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }
    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_conversation_header), null);
    }


    class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Conversation> items;

        public void setItems(ArrayList<Conversation> in) {
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

            View view = null;

            if (viewType == TYPE_SEND)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_send_message, parent, false);
            else if (viewType == TYPE_RECEIVE)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receive_message, parent, false);

            return new ItemAdapter.ItemHolder(view);

        }

        @Override
        public int getItemViewType(int position) {
            if (position % 2 == 0)
                return TYPE_SEND;
            else return TYPE_RECEIVE;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            try {
                if (holder == null)
                    return;


            } catch (Exception ex) {
                LogWrapper.loge("ItemAdapter_onBindViewHolder_Exception: ", ex);
            }


        }

        @Override
        public int getItemCount() {

            return 50;

          //  return (items == null || items.size() == 0) ? 0 : items.size();

        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            CardView lyRoot;
            TextView txtSubject;
            TextView txtMessage;
            TextView txtDate;
            TextView txtState;

            Conversation conversation;


            public ItemHolder(View v) {
                super(v);

                txtSubject = v.findViewById(R.id.txtSubject);
                txtMessage = v.findViewById(R.id.txtMessage);
                txtDate = v.findViewById(R.id.txtDate);
                txtState = v.findViewById(R.id.txtState);

                lyRoot = v.findViewById(R.id.lyRoot);
                FontChanger.applyMainFont(lyRoot);


            }
        }
    }


}