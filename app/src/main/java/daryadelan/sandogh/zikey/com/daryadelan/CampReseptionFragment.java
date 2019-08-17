package daryadelan.sandogh.zikey.com.daryadelan;

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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import daryadelan.sandogh.zikey.com.daryadelan.model.CampReseption;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;

/**
 * Created by Zikey on 23/10/2017.
 */

public class CampReseptionFragment extends DialogFragment {

    private static final String KEY_ALERT_DIALOG = "ALERT_DIALOG";

    private CampReseptionFragment dialog;
    private Toolbar tlb;


    //Views
    private ViewGroup root;
    private RelativeLayout lyBackground;

    private ISaveForm iSaveForm;

    public void setiSaveForm(ISaveForm iSaveForm) {
        this.iSaveForm = iSaveForm;
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

        root = (ViewGroup) inflater.inflate(R.layout.camp_reseption_fragment, null);
        if (root == null)
            return null;

        initDialog();
        initViews();
        initContent();
        initClickListeners();


        return root;

    }


    private void initClickListeners() {


    }


    private void initContent() {

    }

    private void initDialog() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    void initViews() {

        lyBackground = (RelativeLayout) root.findViewById(R.id.lyBackground);
        tlb = (Toolbar) root.findViewById(R.id.tlb);


        tlb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        FontChanger.applyTitleFont(lyBackground);


    }


    public static CampReseptionFragment Show(FragmentActivity act, ISaveForm iSaveForm) {

        try {

            if (iSaveForm == null)
                return null;

            CampReseptionFragment fragment = new CampReseptionFragment();
            fragment.setiSaveForm(iSaveForm);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public interface ISaveForm {

        public void onSaveForm(CampReseption campReseption);

    }


}
