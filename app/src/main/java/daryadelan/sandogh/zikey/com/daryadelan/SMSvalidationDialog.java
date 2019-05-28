package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razanpardazesh.razanlibs.Tools.Convertor;

import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;

/**
 * Created by Zikey on 23/10/2017.
 */

public class SMSvalidationDialog extends DialogFragment {

    private static final String KEY_ALERT_DIALOG = "ALERT_DIALOG";

    private SMSvalidationDialog dialog;

    public boolean isCanceleed = true;
    private boolean pleaseWait = false;

    //Views
    private ViewGroup root;
    private RelativeLayout lyContent;
    private RelativeLayout lyBackground;
    private RelativeLayout lyCountDown;
    private TextView txtTitle;
    private TextView txtComment;
    private TextView txtCountDown;
    private Button btnAction;
    private Button btnClose;

    private OnActionClickListener onOkActionClickListener;
    private OnCancelClickListener onCancleClickListener;

    private String title;
    private String question;
    private String submitText;
    private String cancelText;
    public EditText userInputDialog;

    private TextWatcher textWatcher;



    public SMSvalidationDialog setOnOkActionClickListener(OnActionClickListener onOkActionClickListener) {
        this.onOkActionClickListener = onOkActionClickListener;
        return this;
    }

    public SMSvalidationDialog setOnCancleClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancleClickListener = onCancelClickListener;
        return this;
    }

    public SMSvalidationDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public SMSvalidationDialog setQuestion(String question) {
        this.question = question;
        return this;
    }

    public SMSvalidationDialog setSubmitText(String submitText) {
        this.submitText = submitText;
        return this;
    }

    public SMSvalidationDialog setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

      public SMSvalidationDialog setTextWatcher(TextWatcher watcher) {
        this.textWatcher = watcher;
        return this;
    }



    public SMSvalidationDialog setDialog(SMSvalidationDialog dialog) {
        this.dialog = dialog;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME,
                android.R.style.Theme_Black_NoTitleBar);
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));

        }

        dialog.setCancelable(false);
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if (onCancleClickListener!=null)
//                    onCancleClickListener.onClickCancel(VisitorsAlertDialog.this);
//            }
//        });

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (onCancleClickListener != null)
            onCancleClickListener.onClickOutside(SMSvalidationDialog.this, null);
        super.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.sms_validation_dialog, null);
        if (root == null)
            return null;

        initDialog();
        initViews();
        initContent();
        initClickListeners();


        return root;

    }


    private void initClickListeners() {

        if (onCancleClickListener == null)
            onCancleClickListener = new OnCancelClickListener() {
                @Override
                public void onClickCancel(DialogFragment fragment, String input,boolean isPleaseWait) {
                    if (fragment != null)
                        fragment.dismiss();
                }

                @Override
                public void onClickOutside(DialogFragment fragment, String input) {
                    if (fragment != null)
                        fragment.dismiss();
                }
            };

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCanceleed = false;

//                onCancleClickListener = null;

                if (onOkActionClickListener != null) {
                    if (TextUtils.isEmpty(userInputDialog.getText()))
                        onOkActionClickListener.onClick(dialog, null);
                    else
                        onOkActionClickListener.onClick(dialog, userInputDialog.getText().toString());
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onCancleClickListener != null) {

                    if (pleaseWait) {
                        dismiss();
                        return;
                    }
                    initCountDown();
                    if (TextUtils.isEmpty(userInputDialog.getText())) {
                        onCancleClickListener.onClickCancel(SMSvalidationDialog.this, null,pleaseWait);
                    } else {
                        onCancleClickListener.onClickCancel(SMSvalidationDialog.this, userInputDialog.getText().toString(),pleaseWait);
                    }
                }

            }
        });


        lyBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancleClickListener != null)
                    onCancleClickListener.onClickOutside(SMSvalidationDialog.this, null);

            }
        });

        lyContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    private void initContent() {
        btnClose.setText(getString(R.string.cancel));
        btnAction.setText(R.string.action_yes);

        if (onOkActionClickListener == null)
            btnAction.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
        }

        if (!TextUtils.isEmpty(question)) {
            txtComment.setText(question);
        }

        if (!TextUtils.isEmpty(cancelText)) {
            btnClose.setText(cancelText);
        }

        if (!TextUtils.isEmpty(submitText)) {
            btnAction.setText(submitText);
        }

    }

    private void initDialog() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    void initViews() {

        lyContent = (RelativeLayout) root.findViewById(R.id.lyContent);
        txtTitle = (TextView) root.findViewById(R.id.txtTitle);
        txtComment = (TextView) root.findViewById(R.id.txtComment);
        txtCountDown = (TextView) root.findViewById(R.id.txtCountDown);
        btnAction = (Button) root.findViewById(R.id.btnAction);
        userInputDialog = root.findViewById(R.id.userInputDialog);
        lyBackground = (RelativeLayout) root.findViewById(R.id.lyBackground);
        lyCountDown = (RelativeLayout) root.findViewById(R.id.lyCountDown);

        btnClose = (Button) root.findViewById(R.id.btnClose);

        if (textWatcher!=null){
            userInputDialog.addTextChangedListener(textWatcher);
        }

        fitLayoutSize(lyContent);
        FontChanger.applyMainFont(lyContent);
        FontChanger.applyTitleFont(txtCountDown);

    }

    private void fitLayoutSize(ViewGroup v) {
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = v.getLayoutParams();
        float d = Convertor.toDp(width, getActivity());
        if (d > 350) {
            int p = (int) Convertor.toDp(110, getActivity());
            if (params instanceof RelativeLayout.LayoutParams)
                ((RelativeLayout.LayoutParams) params).setMargins(p, p, p, p);

        }
    }

    public static SMSvalidationDialog Show(FragmentActivity act, String title, String question, String submitText, String cancelText, OnActionClickListener onActionClickListener, OnCancelClickListener onCancelClickListener,TextWatcher watcher) {

        try {


            SMSvalidationDialog fragment = new SMSvalidationDialog();

            fragment.setTitle(title)
                    .setQuestion(question)
                    .setSubmitText(submitText)
                    .setCancelText(cancelText)
                    .setOnOkActionClickListener(onActionClickListener)
                    .setTextWatcher(watcher)
                    .setDialog(fragment)
                    .initCountDown()
                    .setOnCancleClickListener(onCancelClickListener);

            if (!act.isDestroyed())
                fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);

            return fragment;
        } catch (Exception ex) {

            LogWrapper.loge("VisitorsAlertDialog_Show_Exception: ", ex);
        }
        return null;

    }

    @Override
    public void onDestroy() {

        try {
            if (isCanceleed) {
                if (onCancleClickListener != null)
                    onCancleClickListener.onClickCancel(SMSvalidationDialog.this, null,pleaseWait);
            }
        } catch (Exception ex) {
            LogWrapper.loge("VisitorsAlertDialog_onDestroy_Exception: ", ex);
        }

        super.onDestroy();
    }

    public interface OnActionClickListener {

        void onClick(DialogFragment fragment, String input);

    }

    public interface OnCancelClickListener {

        void onClickCancel(DialogFragment fragment, String input, boolean isPleaseWait);

        void onClickOutside(DialogFragment fragment, String input);

    }


    public SMSvalidationDialog initCountDown() {
        if (lyCountDown!=null)
        lyCountDown.setVisibility(View.VISIBLE);
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    pleaseWait = true;
                    btnClose.setText(cancelText);
                    txtCountDown.setText(String.valueOf(millisUntilFinished / 1000));
                } catch (Exception ex) {
                    LogWrapper.loge("VisitorDialogBuilder_onTick_Exception: ", ex);
                }

            }

            @Override
            public void onFinish() {
                try {

                    pleaseWait = false;
                    lyCountDown.setVisibility(View.GONE);
                    btnClose.setText(R.string.retry);
                    if (!getActivity().isDestroyed())
                        Toast.makeText(getActivity(), "در صورتی که کد فعال سازی ارسال نشد، عملیات عضویت را مجددا تکرار نمایید.", Toast.LENGTH_SHORT).show();


                } catch (Exception ex) {
                    LogWrapper.loge("SMSvalidationDialog_onFinish_Exception: ", ex);
                }

               }
        }.start();


        return this;
    }

}
