package daryadelan.sandogh.zikey.com.daryadelan;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.Conversation;
import daryadelan.sandogh.zikey.com.daryadelan.model.Message;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationTopicWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IConversation;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.ConversationServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class ConversationFooterActivity extends AppCompatActivity {

    private static final String KEY_HEADER_ID = "HEADER_ID";
    private final int KEY_OPEN_REQUEST_CODE = 1;

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
    private ImageView imgAttach;
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

        initListeners();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.clearAdapter();
        getData();

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
                        null, false, new CustomAlertDialog.OnActionClickListener() {
                            @Override
                            public void onClick(DialogFragment fragment) {
                                fragment.dismiss();
                                saveMessage();
                            }
                        }, null);
            }
        });

        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionListener dialogPermissionListener =
                        DialogOnDeniedPermissionListener.Builder
                                .withContext(getApplicationContext())
                                .withTitle("مجوز دسترسی به حافظه")
                                .withMessage("جهت دسترسی به فایل ها، نرم افزار نیازمند دسترسی به فایل های دستگاه شما را دارد")
                                .withButtonText(android.R.string.ok)
                                .build();

                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        openTextFile(view);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();


            }
        });
    }

    public void openTextFile(View view) {

//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
//        startActivityForResult(intent, KEY_OPEN_REQUEST_CODE);

        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, KEY_OPEN_REQUEST_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case KEY_OPEN_REQUEST_CODE:
                if (resultCode == -1) {

                    String filepath = data.getData().getPath();
                    filepath = filepath.substring(filepath.indexOf(":") + 1);


                    encodeToBase(filepath);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void saveMessage() {

        if (TextUtils.isEmpty(edtMessage.getText())) {
            new CustomDialogBuilder().showAlert(ConversationFooterActivity.this, "متن پیام را وارد نمایید", false);
            return;
        }

        insertMessage();


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
        imgAttach = (ImageView) findViewById(R.id.imgAttach);
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

    void insertMessage() {

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


    private String encodeToBase(String filePath) {

        if (TextUtils.isEmpty(filePath))
            return null;
        String path = Environment.getExternalStorageDirectory().getPath();
        path = path + "/"+filePath;
        File file = new File(path);


        if (!file.exists()) {
           
            return null;
        }

        byte[] bytesArray = new byte[(int) file.length()];


        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fis.read(bytesArray); //read file into bytes[]
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String encodedString = Base64.encodeToString(bytesArray, Base64.DEFAULT);

        return encodedString;

    }

    private String getFileNameFromUri(Uri uri) {

        String displayName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);

        try {

            if (cursor != null && cursor.moveToFirst()) {

                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }

        return displayName;
    }

    public String getPath(Uri uri) {

        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }


}