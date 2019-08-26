package daryadelan.sandogh.zikey.com.daryadelan.customview;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.razanpardazesh.razanlibs.Tools.Convertor;

import daryadelan.sandogh.zikey.com.daryadelan.R;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;


/**
 * Created by Zikey on 23/10/2017.
 */

public class CustomAlertDialog extends DialogFragment {

    private static final String KEY_ALERT_DIALOG = "ALERT_DIALOG";

    private CustomAlertDialog dialog;

    public boolean isCanceleed = true;

    //Views
    private ViewGroup root;
    private RelativeLayout lyContent;
    private RelativeLayout lyBackground;
    private TextView txtTitle;
    private TextView txtComment;
    private Button btnAction;
    private Button btnClose;

    private OnActionClickListener onOkActionClickListener;
    private OnCancelClickListener onCancleClickListener;

    private String title;
    private String question;
    private String submitText;
    private String cancelText;

    boolean htmlQuestionType = false;

    public CustomAlertDialog setOnOkActionClickListener(OnActionClickListener onOkActionClickListener) {
        this.onOkActionClickListener = onOkActionClickListener;
        return this;
    }

    public CustomAlertDialog setHtmlQuestionType(boolean htmlQuestionType) {
        this.htmlQuestionType = htmlQuestionType;
        return this;
    }

    public CustomAlertDialog setOnCancleClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancleClickListener = onCancelClickListener;
        return this;
    }

    public CustomAlertDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomAlertDialog setQuestion(String question) {
        this.question = question;
        return this;
    }

    public CustomAlertDialog setSubmitText(String submitText) {
        this.submitText = submitText;
        return this;
    }

    public CustomAlertDialog setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public CustomAlertDialog setDialog(CustomAlertDialog dialog) {
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
//                    onCancleClickListener.onClickCancel(CustomAlertDialog.this);
//            }
//        });

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (onCancleClickListener != null)
            onCancleClickListener.onClickOutside(CustomAlertDialog.this);
        super.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.custom_alert_dialog, null);
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
                public void onClickCancel(DialogFragment fragment) {
                    if (fragment != null)
                        fragment.dismiss();
                }

                @Override
                public void onClickOutside(DialogFragment fragment) {
                    if (fragment != null)
                        fragment.dismiss();
                }
            };


        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCanceleed = false;

                onCancleClickListener = null;

                if (onOkActionClickListener != null) {
                    onOkActionClickListener.onClick(dialog);
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancleClickListener != null)
                    onCancleClickListener.onClickCancel(CustomAlertDialog.this);
            }
        });


        lyBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancleClickListener != null)
                    onCancleClickListener.onClickOutside(CustomAlertDialog.this);

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtComment.setText(Html.fromHtml(question, Html.FROM_HTML_MODE_COMPACT));
            } else {
                txtComment.setText(Html.fromHtml(question));
            }

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
        btnAction = (Button) root.findViewById(R.id.btnAction);

        lyBackground = (RelativeLayout) root.findViewById(R.id.lyBackground);

        btnClose = (Button) root.findViewById(R.id.btnClose);

        fitLayoutSize(lyContent);
        FontChanger.applyMainFont(lyContent);

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

    public static CustomAlertDialog Show(FragmentActivity act, String title, String question, String submitText, String cancelText, OnActionClickListener onActionClickListener, OnCancelClickListener onCancelClickListener) {
        try {

            CustomAlertDialog fragment = new CustomAlertDialog();

            fragment.setTitle(title)
                    .setQuestion(question)
                    .setSubmitText(submitText)
                    .setCancelText(cancelText)
                    .setOnOkActionClickListener(onActionClickListener)
                    .setDialog(fragment)
                    .setOnCancleClickListener(onCancelClickListener);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!act.isDestroyed())
                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
            } else {
                if (!act.isFinishing())
                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
            }

            return fragment;
        } catch (Exception ex) {

            LogWrapper.loge("VisitorsAlertDialog_Show_Exception: ", ex);
        }
        return null;

    }

    public static CustomAlertDialog Show(FragmentActivity act, boolean htmlQuestionType, String title, String question, String submitText, String cancelText, OnActionClickListener onActionClickListener, OnCancelClickListener onCancelClickListener) {
        try {

            CustomAlertDialog fragment = new CustomAlertDialog();

            fragment.setTitle(title)
                    .setQuestion(question)
                    .setSubmitText(submitText)
                    .setCancelText(cancelText)
                    .setOnOkActionClickListener(onActionClickListener)
                    .setHtmlQuestionType(htmlQuestionType)
                    .setDialog(fragment)
                    .setOnCancleClickListener(onCancelClickListener);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!act.isDestroyed())
                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
            } else {
                if (!act.isFinishing())
                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
            }

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
                    onCancleClickListener.onClickCancel(CustomAlertDialog.this);
            }
        } catch (Exception ex) {
            LogWrapper.loge("VisitorsAlertDialog_onDestroy_Exception: ", ex);
        }

        super.onDestroy();
    }

    public interface OnActionClickListener {

        void onClick(DialogFragment fragment);

    }

    public interface OnCancelClickListener {

        void onClickCancel(DialogFragment fragment);

        void onClickOutside(DialogFragment fragment);

    }


}
