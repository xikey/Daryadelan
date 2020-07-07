package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.razanpardazesh.razanlibs.CustomView.DialogBuilder;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomFullscreenLoader;
import daryadelan.sandogh.zikey.com.daryadelan.model.CampReseption;
import daryadelan.sandogh.zikey.com.daryadelan.model.ConversationTopic;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ConversationTopicWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IConversation;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.ConversationServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import es.dmoral.toasty.Toasty;

/**
 * Created by Zikey on 23/10/2017.
 */

public class NewConversationFragment extends DialogFragment {

    private static final String KEY_ALERT_DIALOG = "ALERT_DIALOG";

    private NewConversationFragment dialog;
    private Toolbar tlb;

    private IConversation conversationRepo;
    private User user;

    //Views
    private ViewGroup root;
    private RelativeLayout lyBackground;

    private ISaveForm iSaveForm;

    private EditText edtSubject;
    private EditText edtMessage;


    private CardView crdSaveFrom;
    private CardView crdDelete;
    private ConversationTopic conversationTopic;
    private int position;


    private boolean editMode;
    int selectedRelationPos = -1;

    public void setiSaveForm(ISaveForm iSaveForm) {
        this.iSaveForm = iSaveForm;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public ConversationTopic getConversationTopic() {
        return conversationTopic;
    }

    public void setConversationTopic(ConversationTopic conversationTopic) {
        this.conversationTopic = conversationTopic;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_FRAME,
                android.R.style.Theme_Black_NoTitleBar);
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));

        }

        dialog.setCancelable(false);


        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.new_conversation_topic_fragment, null);
        if (root == null)
            return null;

        initDialog();
        initViews();
        initRepo();
        initContent();
        initClickListeners();


        return root;

    }

    private void initRepo() {

        if (conversationRepo == null)
            conversationRepo = new ConversationServerRepo();
    }


    private void initClickListeners() {

        crdSaveFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveForm();

            }
        });

        crdDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iSaveForm != null)
                    iSaveForm.onRemove(position);
                dismiss();
            }
        });


    }


    private void initContent() {

        if (editMode) {
            crdDelete.setVisibility(View.VISIBLE);
        } else {
            crdDelete.setVisibility(View.GONE);
        }


    }

    private void initDialog() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    void initViews() {

        lyBackground = (RelativeLayout) root.findViewById(R.id.lyBackground);
        tlb = (Toolbar) root.findViewById(R.id.tlb);

        edtSubject = root.findViewById(R.id.edtSubject);
        edtMessage = root.findViewById(R.id.edtMessage);

        crdSaveFrom = root.findViewById(R.id.crdSaveFrom);
        crdDelete = root.findViewById(R.id.crdRemove);


        tlb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        FontChanger.applyTitleFont(lyBackground);


    }


    public static NewConversationFragment Show(FragmentActivity act, ISaveForm iSaveForm, User user) {

        try {

            if (iSaveForm == null)
                return null;

            if (user == null)
                return null;

            NewConversationFragment fragment = new NewConversationFragment();
            fragment.setiSaveForm(iSaveForm);
            fragment.setUser(user);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!act.isDestroyed())
                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
            }

            return fragment;
        } catch (Exception ex) {

            LogWrapper.loge("VisitorsAlertDialog_Show_Exception: ", ex);
        }
        return null;

    }

//    public static NewConversationFragment Show_EditableMode(FragmentActivity act, ISaveForm iSaveForm, CampReseption campReseption, int position) {
//
//        try {
//
//            if (iSaveForm == null)
//                return null;
//
//            NewConversationFragment fragment = new NewConversationFragment();
//            fragment.setiSaveForm(iSaveForm);
//            fragment.setPosition(position);
//            fragment.setEditMode(true);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                if (!act.isDestroyed())
//                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
//            }
//
//            return fragment;
//        } catch (Exception ex) {
//
//            LogWrapper.loge("VisitorsAlertDialog_Show_Exception: ", ex);
//        }
//        return null;
//
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public interface ISaveForm {

        public void onSaveForm(ConversationTopic conversationTopic,long messageID);

        void onEdit(ConversationTopic conversationTopic, int pos);

        void onRemove(int pos);

    }


    private void saveForm() {

        if (TextUtils.isEmpty(edtSubject.getText())) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "فیلد موضوع خالی میباشد!",false);
            return;
        }

        if (TextUtils.isEmpty(edtMessage.getText())) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "فیلد پیام خالی میباشد!",false);
            return;
        }


        try {
            conversationTopic = new ConversationTopic();
            conversationTopic.setSubject(edtSubject.getText().toString());
            conversationTopic.setMessageText(edtMessage.getText().toString());

            new CustomDialogBuilder().showYesNOCustomAlert((AppCompatActivity) getActivity(), "ارسال پیام", "مایل به ارسال پیام میباشید؟", "ارسال", "انصراف",false, new CustomAlertDialog.OnActionClickListener() {
                @Override
                public void onClick(DialogFragment fragment) {
                    sendData();
                    fragment.dismiss();
                }
            }, null);


        } catch (Exception e) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "خطا در ذخیره سازی اطلاعات. لطفا فیلد های ورودی را مجددا بررسی نمایید",false);
            e.printStackTrace();
        }


    }

    private void sendData() {

        if (conversationRepo == null)
            return;

        if (conversationTopic == null)
            return;

        CustomFullscreenLoader customFullscreenLoader = new CustomDialogBuilder().loader((AppCompatActivity) getActivity());


        conversationRepo.insertConversationTopic(getActivity().getApplicationContext(), user.getTokenType(), user.getToken(), conversationTopic, new IRepoCallBack<ConversationTopicWrapper>() {
            @Override
            public void onAnswer(ConversationTopicWrapper answer) {

                if (customFullscreenLoader!=null)
                    customFullscreenLoader.dismiss();

                if (answer != null && answer.getData() != 0) {
                    Toasty.success((AppCompatActivity) getActivity(), "پیام شما با موفقیت ارسال شد").show();
                    if (iSaveForm != null)
                        iSaveForm.onSaveForm(conversationTopic,answer.getData());
                    dismiss();
                } else {
                    Toasty.success((AppCompatActivity) getActivity(), "خطا در ارسال اطلاعات، لطفا مجددا تلاش نمایید").show();
                }

            }

            @Override
            public void onError(Throwable error) {
                if (customFullscreenLoader!=null)
                    customFullscreenLoader.dismiss();
                Toasty.success((AppCompatActivity) getActivity(), "خطا در ارسال اطلاعات، لطفا مجددا تلاش نمایید").show();
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
