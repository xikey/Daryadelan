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
import android.support.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razanpardazesh.razanlibs.Tools.Convertor;

import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;

/**
 * Created by Zikey on 23/10/2017.
 */

public class SMSvalidationDialogFullScreen extends DialogFragment {

    private static final String KEY_ALERT_DIALOG = "ALERT_DIALOG";

    private SMSvalidationDialogFullScreen dialog;

    public boolean isCanceleed = true;
    private boolean pleaseWait = false;

    private IInputTextWatcher iInputTextWatcher;

    //Views
    private ViewGroup root;
    private RelativeLayout lyContent;
    private RelativeLayout lyBackground;
    private ImageView btnBack;

    private TextView txtInputOne;
    private TextView txtInputTwo;
    private TextView txtInputThree;
    private TextView txtInputFour;
    private TextView txtInputFive;

    private TextView btnOne;
    private TextView btnTwo;
    private TextView btnThree;
    private TextView btnFour;
    private TextView btnFive;
    private TextView btnSix;
    private TextView btnSeven;
    private TextView btnEight;
    private TextView btnNine;
    private TextView btnZero;
    private ImageView btnDelete;


    private String inputValidation = "";


    public SMSvalidationDialogFullScreen setDialog(SMSvalidationDialogFullScreen dialog) {
        this.dialog = dialog;
        return this;
    }


    public SMSvalidationDialogFullScreen fillFromSMS(String input) {
        if (!TextUtils.isEmpty(input))
            inputValidation = input;

        return this;


    }

    public SMSvalidationDialogFullScreen setiInputTextWatcher(IInputTextWatcher iInputTextWatcher) {
        this.iInputTextWatcher = iInputTextWatcher;
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


        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.sms_validation_dialog2, null);
        if (root == null)
            return null;

        initDialog();
        initViews();
        initContent();
        initClickListeners();


        return root;

    }


    private void initClickListeners() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("1", false);
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("2", false);
            }
        });
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("3", false);
            }
        });
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("4", false);
            }
        });
        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("5", false);
            }
        });
        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("6", false);
            }
        });
        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("7", false);
            }
        });
        btnEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("8", false);
            }
        });
        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("9", false);
            }
        });
        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed("0", false);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPressed(null, true);
            }
        });


    }


    private void initContent() {

    }

    private void initDialog() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    void initViews() {

        lyBackground = (RelativeLayout) root.findViewById(R.id.lyBackground);

        btnBack = (ImageView) root.findViewById(R.id.btnBack);

        txtInputOne = root.findViewById(R.id.txtInputOne);
        txtInputTwo = root.findViewById(R.id.txtInputTwo);
        txtInputThree = root.findViewById(R.id.txtInputThree);
        txtInputFour = root.findViewById(R.id.txtInputFour);
        txtInputFive = root.findViewById(R.id.txtInputFive);

        btnOne = root.findViewById(R.id.btnOne);
        btnTwo = root.findViewById(R.id.btnTwo);
        btnThree = root.findViewById(R.id.btnThree);
        btnFour = root.findViewById(R.id.btnFour);
        btnFive = root.findViewById(R.id.btnFive);
        btnSix = root.findViewById(R.id.btnSix);
        btnSeven = root.findViewById(R.id.btnSeven);
        btnEight = root.findViewById(R.id.btnEight);
        btnNine = root.findViewById(R.id.btnNine);
        btnZero = root.findViewById(R.id.btnZero);
        btnDelete = root.findViewById(R.id.btnDelete);

        FontChanger.applyTitleFont(lyBackground);
        FontChanger.applyMainFont(root.findViewById(R.id.lyNumbers));

    }


    public static SMSvalidationDialogFullScreen Show(FragmentActivity act) {

        try {


            SMSvalidationDialogFullScreen fragment = new SMSvalidationDialogFullScreen();


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

    public interface OnActionClickListener {

        void onClick(DialogFragment fragment, String input);

    }

    public interface OnCancelClickListener {

        void onClickCancel(DialogFragment fragment, String input, boolean isPleaseWait);

        void onClickOutside(DialogFragment fragment, String input);

    }

    private void btnPressed(String input, boolean deleteLastChar) {

        if (deleteLastChar) {
            if (TextUtils.isEmpty(inputValidation))
                return;

            int size = inputValidation.length();
            inputValidation = inputValidation.substring(0, size - 1);
        } else {
            if (inputValidation != null && inputValidation.length() < 5) {

                inputValidation += input;

            }
        }


        fillInputBox();

    }

    private void fillInputBox() {

        txtInputOne.setText("");
        txtInputTwo.setText("");
        txtInputThree.setText("");
        txtInputFour.setText("");
        txtInputFive.setText("");

        if (TextUtils.isEmpty(inputValidation)) {

            return;
        }

        for (int i = 0; i < inputValidation.length(); i++) {
            String tmp = String.valueOf(inputValidation.charAt(i));

            if (i == 0) {
                txtInputOne.setText(tmp);
            } else if (i == 1) {
                txtInputTwo.setText(tmp);
            } else if (i == 2) {
                txtInputThree.setText(tmp);
            } else if (i == 3) {
                txtInputFour.setText(tmp);
            } else if (i == 4) {
                txtInputFive.setText(tmp);
            }

        }

        checkInputValidation();
    }

    private void checkInputValidation() {
        if (TextUtils.isEmpty(inputValidation))
            return;

        if (inputValidation.length() == 5) {
            if (iInputTextWatcher != null)
                iInputTextWatcher.onDone(inputValidation);
        }
    }

    public interface IInputTextWatcher {

        public void onDone(String input);

    }


}
