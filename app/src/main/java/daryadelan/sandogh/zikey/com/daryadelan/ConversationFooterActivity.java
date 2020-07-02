package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
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

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Conversation;
import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.Message;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationTopicWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IConversation;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.ConversationServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class ConversationFooterActivity extends AppCompatActivity {

    private static final String KEY_HEADER_ID = "HEADER_ID";

    private static int TYPE_SEND = 1;
    private static int TYPE_RECEIVE = 2;

    private long headerID;
    private RecyclerView rvItem;
    private ItemAdapter adapter;
    private LinearLayout lyProgress;
    private LinearLayout lyBottomProgress;
    private IConversation conversationRepo;
    private User user;
    private int page = 0;
    private int hasMore = 0;
    private ImageView imgSend;
    private EditText edtMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_footer);

        initRepo();
        parseIntent();
        initToolbar();
        getUserData();
        initViews();
        initRecycleView();
        getData();
        initListeners();


    }

    private void parseIntent() {
        Intent data = getIntent();
        if (data.hasExtra(KEY_HEADER_ID))
            headerID = data.getLongExtra(KEY_HEADER_ID, 0);
    }

    public static void start(Context context, long messageID) {
        Intent starter = new Intent(context, ConversationFooterActivity.class);
        starter.putExtra(KEY_HEADER_ID, messageID);
        context.startActivity(starter);
    }

    private void initListeners() {
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomDialogBuilder().showYesNOCustomAlert(ConversationFooterActivity.this, "ارسال پیام", "مایل به ارسال پیام میباشید؟", "ارسال",
                        null, new CustomAlertDialog.OnActionClickListener() {
                            @Override
                            public void onClick(DialogFragment fragment) {
                                fragment.dismiss();
                                saveMessage();
                            }
                        }, null);
            }
        });
    }

    private void saveMessage() {

        if (TextUtils.isEmpty(edtMessage.getText())) {
            new CustomDialogBuilder().showAlert(ConversationFooterActivity.this, "متن پیام را وارد نمایید");
            return;
        }

        insertFakeMessage();


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

        conversationRepo.getAllConversationsByID(getApplicationContext(), user.getTokenType(), user.getToken(), headerID, new IRepoCallBack<ConversationWrapper>() {
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

                    try {
                        adapter.setItems(answer.getData().get(0).getMessages());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


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
        imgSend = (ImageView) findViewById(R.id.imgSend);
        lyBottomProgress = (LinearLayout) findViewById(R.id.lyBottomProgress);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        lyProgress.setVisibility(View.VISIBLE);


    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new ItemAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(ConversationFooterActivity.this, LinearLayoutManager.VERTICAL, false));
        rvItem.setAdapter(adapter);

//        rvItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (hasMore == 0)
//                    return;
//
//                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    int pos = layoutManager.findLastVisibleItemPosition();
//                    if (adapter != null) {
//                        if (adapter.getItemCount() - 1 == pos) {
//                            page++;
//                            getData();
//                        }
//
//                    }
//                }
//
//            }
//        });


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

        private ArrayList<Message> items;

        public void setItems(ArrayList<Message> in) {
            if (items == null)
                items = new ArrayList<>();
            items.addAll(in);
            notifyDataSetChanged();
        }


        void clearAdapter() {

            if (items != null)
                items.clear();

            items = null;

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
            if (items.get(position).isFromUser())
                return TYPE_SEND;
            else return TYPE_RECEIVE;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            try {
                if (holder == null)
                    return;
                Message message = items.get(position);
                ItemHolder hld = (ItemHolder) holder;

                hld.txtMessage.setText(message.getMessageText());
                hld.txtDate.setText(message.getMessageCreateAt());


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

            TextView txtMessage;
            TextView txtDate;


            Conversation conversation;


            public ItemHolder(View v) {
                super(v);

                txtMessage = v.findViewById(R.id.txtMessage);
                txtDate = v.findViewById(R.id.txtDate);

                lyRoot = v.findViewById(R.id.lyRoot);
                FontChanger.applyMainFont(lyRoot);


            }
        }
    }

    void insertFakeMessage() {

        if (conversationRepo == null)
            return;

        conversationRepo.insertMessage(getApplicationContext(), user.getTokenType(), user.getToken(), edtMessage.getText().toString(), headerID, "0", new IRepoCallBack<ConversationTopicWrapper>() {
            @Override
            public void onAnswer(ConversationTopicWrapper answer) {
                edtMessage.getText().clear();
                adapter.clearAdapter();
                getData();
            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });
    }


}