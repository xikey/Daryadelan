package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.model.CampReseption;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;

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

    private EditText edtName;
    private EditText edtFamily;
    private EditText edtNationalCode;
    private EditText edtRelation;

    private CardView crdSaveFrom;
    private CardView crdDelete;
    private RelativeLayout lyPickCount;
    private CampReseption campReseption;
    private int position;

    private boolean editMode;
    int selectedRelationPos = -1;

    public void setiSaveForm(ISaveForm iSaveForm) {
        this.iSaveForm = iSaveForm;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setCampReseption(CampReseption campReseption) {
        this.campReseption = campReseption;
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

        lyPickCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRelation();
            }
        });

        edtRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickRelation();
            }
        });

    }


    private void initContent() {

        if (editMode) {
            crdDelete.setVisibility(View.VISIBLE);
        } else {
            crdDelete.setVisibility(View.GONE);
        }

        try {
            edtName.setText(campReseption.getName());
            edtFamily.setText(campReseption.getFamily());
            edtNationalCode.setText("" + campReseption.getNationalCode());
            edtRelation.setText("" + campReseption.getRelationShipName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initDialog() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    void initViews() {

        lyBackground = (RelativeLayout) root.findViewById(R.id.lyBackground);
        tlb = (Toolbar) root.findViewById(R.id.tlb);

        edtName = root.findViewById(R.id.edtName);
        edtFamily = root.findViewById(R.id.edtFamily);
        edtNationalCode = root.findViewById(R.id.edtNationalCode);
        edtRelation = root.findViewById(R.id.edtRelation);

        crdSaveFrom = root.findViewById(R.id.crdSaveFrom);
        crdDelete = root.findViewById(R.id.crdRemove);

        lyPickCount = root.findViewById(R.id.lyPickCount);

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

    public static CampReseptionFragment Show_EditableMode(FragmentActivity act, ISaveForm iSaveForm, CampReseption campReseption, int position) {

        try {

            if (iSaveForm == null)
                return null;

            CampReseptionFragment fragment = new CampReseptionFragment();
            fragment.setiSaveForm(iSaveForm);
            fragment.setCampReseption(campReseption);
            fragment.setPosition(position);
            fragment.setEditMode(true);

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

        void onEdit(CampReseption campReseption, int pos);

        void onRemove(int pos);

    }


    private void saveForm() {

        if (TextUtils.isEmpty(edtName.getText())) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "فیلد نام خالی میباشد!");
            return;
        }

        if (TextUtils.isEmpty(edtFamily.getText())) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "فیلد نام خانوادگی خالی میباشد!");
            return;
        }

        if (TextUtils.isEmpty(edtNationalCode.getText())) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "کد ملی وارد شده نادرست میباشد");
            return;
        }

        if (!isValidNationalCode(edtNationalCode.getText().toString()))
            return;


        if (TextUtils.isEmpty(edtRelation.getText())) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "فیلد نسبت خالی میباشد!");
            return;
        }


        try {
            campReseption = new CampReseption();
            campReseption.setName(edtName.getText().toString());
            campReseption.setFamily(edtFamily.getText().toString());
            campReseption.setNationalCode(Long.parseLong(edtNationalCode.getText().toString()));
            campReseption.setRelation(selectedRelationPos);


            if (editMode) {
                if (iSaveForm != null)
                    iSaveForm.onEdit(campReseption, position);
                dismiss();

            } else {
                if (iSaveForm != null)
                    iSaveForm.onSaveForm(campReseption);
                dismiss();
            }


        } catch (Exception e) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "خطا در ذخیره سازی اطلاعات. لطفا فیلد های ورودی را مجددا بررسی نمایید");
            e.printStackTrace();
        }


    }


    public Boolean isValidNationalCode(String nationalCode) {
        //در صورتی که کد ملی وارد شده تهی باشد

        if (TextUtils.isEmpty(nationalCode)) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "مقدار کد ملی نمیتواند خالی باشد");
            return false;
        }

        //در صورتی که کد ملی وارد شده طولش کمتر از 10 رقم باشد
        if (nationalCode.length() != 10) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "مقدار کد ملی باید ده رقم باشد");
            return false;
        }

        //در صورتی که کد ملی ده رقم عددی نباشد
        if (!TextUtils.isDigitsOnly(nationalCode)) {
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "مقدار کد ملی نمیتواند شامل حروف باشد");
            return false;
        }

        //در صورتی که رقم‌های کد ملی وارد شده یکسان باشد
        String allDigitEqual[] = new String[]{
                "0000000000", "1111111111", "2222222222", "3333333333", "4444444444", "5555555555", "6666666666", "7777777777", "8888888888", "9999999999"
        };

        for (int i = 0; i < allDigitEqual.length; i++) {
            if (allDigitEqual[i].equals(nationalCode)) {
                new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "مقدار کد ملی نادرست میباشد");
                return false;
            }
        }

        try {

            long num0 = Long.parseLong("" + nationalCode.charAt(0)) * 10;
            long num1 = Long.parseLong("" + nationalCode.charAt(1)) * 9;
            long num2 = Long.parseLong("" + nationalCode.charAt(2)) * 8;
            long num3 = Long.parseLong("" + nationalCode.charAt(3)) * 7;
            long num4 = Long.parseLong("" + nationalCode.charAt(4)) * 6;
            long num5 = Long.parseLong("" + nationalCode.charAt(5)) * 5;
            long num6 = Long.parseLong("" + nationalCode.charAt(6)) * 4;
            long num7 = Long.parseLong("" + nationalCode.charAt(7)) * 3;
            long num8 = Long.parseLong("" + nationalCode.charAt(8)) * 2;
            long a = Long.parseLong("" + nationalCode.charAt(9));

            long b = (((((((num0 + num1) + num2) + num3) + num4) + num5) + num6) + num7) + num8;
            long c = b % 11;

            if (!(((c < 2) && (a == c)) || ((c >= 2) && ((11 - c) == a)))) {
                new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "مقدار کد ملی نادرست میباشد");
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "مقدار کد ملی نادرست میباشد");
            return false;
        }

    }

    private void pickRelation() {

        CampReseption cmpr = new CampReseption();
        ArrayList<CampReseption> availableRelations = cmpr.getAllAvailableRelations();

        if (availableRelations == null)
            return;

        CharSequence items[] = new CharSequence[availableRelations.size()];

        for (int i = 0; i < availableRelations.size(); i++) {
            items[i] = availableRelations.get(i).getRelationShipName();
        }

        try {
            new CustomDialogBuilder().showPickerDialog((AppCompatActivity) getActivity(), "انتخاب نسبت", items, new CustomDialogBuilder.OnPickerDialogListener() {
                @Override
                public void onPick(int position) {
                    if (editMode) {
                        campReseption.setRelation(position);
                        edtRelation.setText(campReseption.getRelationShipName());
                        selectedRelationPos = position;
                        initContent();
                    } else {
                        CampReseption cmpr=new CampReseption();
                        cmpr.setRelation(position);
                        edtRelation.setText(cmpr.getRelationShipName());
                        selectedRelationPos = position;
                        initContent();
                    }

                }
            });
        } catch (Exception ex) {
            LogWrapper.loge("ChequesActivity_onClick_Exception: ", ex);
        }


    }


}
