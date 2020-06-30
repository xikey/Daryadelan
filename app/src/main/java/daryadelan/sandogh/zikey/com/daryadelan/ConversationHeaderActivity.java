package daryadelan.sandogh.zikey.com.daryadelan;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Conversation;
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

public class ConversationHeaderActivity extends AppCompatActivity {

    private RecyclerView rvItem;
    private ItemAdapter adapter;
    private LinearLayout lyProgress;
    private IConversation conversationRepo;
    private User user;
    private ImageView imgBackground;
    private CardView crdNewConversation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_header);

        initRepo();
        initToolbar();
        getUserData();
        initViews();
        initRecycleView();
        getData();


    }

    private void getData() {

        if (user == null) {
            Toasty.error(ConversationHeaderActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }

        if (conversationRepo == null)
            return;

        lyProgress.setVisibility(View.VISIBLE);

        conversationRepo.getAllConversationsTopics(getApplicationContext(), user.getTokenType(), user.getToken(), new IRepoCallBack<ConversationWrapper>() {
            @Override
            public void onAnswer(ConversationWrapper answer) {
                lyProgress.setVisibility(View.GONE);
                if (answer != null && answer.getData() != null && answer.getData().size() != 0) {
                    adapter.setItems(answer.getData());
                } else {
                    Toasty.info(ConversationHeaderActivity.this, "گفتگویی ایجاد نشده است، جهت ثبت گفتگوی جدید دکمه زیر صفحه را لمس نمایید").show();
                }
            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                Toasty.info(ConversationHeaderActivity.this, "گفتگویی ایجاد نشده است، جهت ثبت گفتگوی جدید دکمه زیر صفحه را لمس نمایید").show();
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
        lyProgress.setVisibility(View.VISIBLE);
        imgBackground = findViewById(R.id.imgBackground);
        crdNewConversation = findViewById(R.id.crdNewConversation);
        FontChanger.applyTitleFont(crdNewConversation);

        new ImageViewWrapper(getApplicationContext()).into(imgBackground).loadBlur(R.drawable.bg_message_full_screen);

    }


    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, ConversationHeaderActivity.class);
        context.startActivity(starter);
    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(ConversationHeaderActivity.this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);


    }

    private void initRepo() {

        if (conversationRepo == null)
            conversationRepo = new ConversationServerRepo();

    }

    private void getUserData() {
        user = UserInstance.getInstance().getUser();
        if (user == null) {
            Toasty.error(ConversationHeaderActivity.this, "خطا در دریافت اطلاعات کاربری").show();
            finish();
        }
    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_conversation_header), null);
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conversation_topics, parent, false);
            return new ItemAdapter.ItemHolder(view);

        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemHolder holder, int position) {


            try {
                if (holder == null)
                    return;


                Conversation conversation = items.get(position);
                holder.txtSubject.setText(conversation.getSubject());
//                holder.txtMessage.setText(conversation.get);
                holder.txtState.setText(conversation.getStatusPersianName());
                holder.txtDate.setText(conversation.getCreateDate());

            } catch (Exception ex) {
                LogWrapper.loge("ItemAdapter_onBindViewHolder_Exception: ", ex);
            }


        }

        @Override
        public int getItemCount() {

            return (items == null || items.size() == 0) ? 0 : items.size();

        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            CardView lyRoot;
            TextView txtSubject;
            TextView txtMessage;
            TextView txtDate;
            TextView txtState;


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
