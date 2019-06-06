package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.razanpardazesh.razanlibs.Tools.ValidateHelper;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.UserServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ToolbarWrapper;
import es.dmoral.toasty.Toasty;

public class CreateUserActivity extends AppCompatActivity {

    private static final String KEY_ACCEPT_CODE = "ACCEPT_CODE";
    private static final String KEY_USER_TYPE = "USER_TYPE";
    private static final String KEY_USER_MOBILE = "USER_MOBILE";

    //Views
    private TextView txtAction;
    private TextInputLayout tilName;
    private EditText edtName;

    private TextInputLayout tilFamily;
    private EditText edtFamily;

    private TextInputLayout tilMobile;
    private EditText edtMobile;

    private TextInputLayout tilPassword;
    private EditText edtPassword;

    private TextInputLayout tilRepeatPassword;
    private EditText edtRepeatPassword;

    private LinearLayout lyProgress;

    private User user;
    private IUser repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        initToolbar();
        initViews();
        parseIntent();
        initRepo();
        initActionButton();
        initTabIndex();
        initClickListeners();

    }

    private void parseIntent() {
        Intent data = getIntent();
        if (data == null)
            return;

        if (data.hasExtra(KEY_ACCEPT_CODE)) {
            if (user == null)
                user = new User();
            user.setAcceptCode(data.getStringExtra(KEY_ACCEPT_CODE));
        }

        if (data.hasExtra(KEY_USER_MOBILE)) {
            if (user == null)
                user = new User();
            user.setMobile(data.getStringExtra(KEY_USER_MOBILE));

            if (edtMobile!=null)
                edtMobile.setText(user.getMobile());
        }

        if (data.hasExtra(KEY_USER_TYPE)) {
            if (user == null)
                user = new User();
            user.setPersonType(data.getStringExtra(KEY_USER_TYPE));

            String message="با سلام"+"\n"+user.getPersonTypeName()+" گرامی"+" خوش آمدید"+"\n"+"جهت ایجاد حساب کاربری اطلاعات صفحه را با دقت تکمیل نمایید.";

            new CustomDialogBuilder().showAlert(CreateUserActivity.this,message);
        }


    }

    @Override
    public void onBackPressed() {
        exitQuestion();

    }

    private void initClickListeners() {

    }

    private void initRepo() {
        if (repo == null)
            repo = new UserServerRepo();
    }


    private void initViews() {

        txtAction = (TextView) findViewById(R.id.txtAction);
        tilName = (TextInputLayout) findViewById(R.id.tilName);
        edtName = (EditText) findViewById(R.id.edtName);
        tilFamily = (TextInputLayout) findViewById(R.id.tilFamily);
        edtFamily = (EditText) findViewById(R.id.edtFamily);
        tilMobile = (TextInputLayout) findViewById(R.id.tilMobile);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        tilRepeatPassword = (TextInputLayout) findViewById(R.id.tilRepeatPassword);
        edtRepeatPassword = (EditText) findViewById(R.id.edtRepeatPassword);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);

    }

    public static void start(FragmentActivity context, String acceptCode,String userType,String mobile,int requestCode) {
        if (acceptCode == null)
            return;

        Intent starter = new Intent(context, CreateUserActivity.class);
        starter.putExtra(KEY_ACCEPT_CODE, acceptCode);
        starter.putExtra(KEY_USER_TYPE, userType);
        starter.putExtra(KEY_USER_MOBILE, mobile);
        context.startActivityForResult(starter,requestCode);
    }

    private void initToolbar() {

        new ToolbarWrapper(this).initToolbarWithBackArrow(R.id.toolbar, getString(R.string.title_activity_create_user), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitQuestion();
            }
        });
    }

    private void initActionButton() {

        txtAction.setText("عضویت");
        FontChanger.applyTitleFont(txtAction);
        final View action = (View) txtAction.getParent();
        if (action == null)
            return;

        action.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });
    }

    private void initTabIndex() {

        edtName.requestFocus();
        edtName.setNextFocusDownId(R.id.edtFamily);
        edtFamily.setNextFocusDownId(R.id.edtPassword);
        edtPassword.setNextFocusDownId(R.id.edtRepeatPassword);

        edtRepeatPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtRepeatPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginValidation();
                }
                return false;
            }
        });

    }

    private void exitQuestion() {

        new CustomDialogBuilder().showYesNOCustomAlert(this, "انصراف", getString(R.string.wanna_exit), getString(R.string.yes), getString(R.string.no), new CustomAlertDialog.OnActionClickListener() {
            @Override
            public void onClick(DialogFragment fragment) {
                finish();
                fragment.dismiss();

            }
        }, new CustomAlertDialog.OnCancelClickListener() {
            @Override
            public void onClickCancel(DialogFragment fragment) {

                fragment.dismiss();
            }

            @Override
            public void onClickOutside(DialogFragment fragment) {
                fragment.dismiss();
            }
        });

    }

    private void validateLogin() {

        if (TextUtils.isEmpty(user.getAcceptCode())) {
            new CustomDialogBuilder().showAlert(CreateUserActivity.this, "متاسفانه خطایی در ثبت کد فعال سازی ب وچود آمده. لطفا عملیات فعال سازی را مجددا نکرار نمایید", new CustomAlertDialog.OnCancelClickListener() {
                @Override
                public void onClickCancel(DialogFragment fragment) {
                    fragment.dismiss();
                    finish();
                }

                @Override
                public void onClickOutside(DialogFragment fragment) {
                    fragment.dismiss();
                    finish();
                }
            });
            return;
        }

        if (!ValidateHelper.validateNecessaryString(edtName.getEditableText())) {
            tilName.setError(ValidateHelper.createMustFillError(this, getString(R.string.name)));
            edtName.requestFocus();
            return;
        } else {
            tilName.setError("");
        }
        user.setFirstName(edtName.getText().toString().trim());


        if (!ValidateHelper.validateNecessaryString(edtFamily.getEditableText())) {
            tilFamily.setError(ValidateHelper.createMustFillError(this, getString(R.string.family)));
            edtFamily.requestFocus();
            return;
        } else {
            tilFamily.setError("");
        }
        user.setLastName(edtFamily.getText().toString().trim());


        if (!ValidateHelper.validateIranCellphone(edtMobile.getEditableText())) {
            tilMobile.setError(ValidateHelper.createIsWrongError(this, getString(R.string.mobile_number)));
            edtMobile.requestFocus();
            return;

        } else {
            tilMobile.setError("");
        }
        user.setMobile(edtMobile.getText().toString());


        if (!isPasswordValid())
            return;
        else {

            user.setPassword(edtPassword.getText().toString());

        }

        createUser();

    }

    private void createUser() {
        if (repo == null)
            return;
        lyProgress.setVisibility(View.VISIBLE);
        repo.createUser(getApplicationContext(), user, new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {
                lyProgress.setVisibility(View.GONE);

                if (answer.getResultId() != 0) {
                    new CustomDialogBuilder().showAlert(CreateUserActivity.this, "خطا در ذخیره سازی اطلاعات");
                    return;
                }


                if (!TextUtils.isEmpty(answer.getMessagee())) {

                    Toasty.info(CreateUserActivity.this, answer.getMessagee()).show();
                }
                login();

            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                new CustomDialogBuilder().showAlert(CreateUserActivity.this, error.getMessage());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });

    }

    private Boolean isPasswordValid() {

        String pass;
        String repeatPass;

        if (tilPassword == null) {
            initViews();
        }

        tilPassword.setError(null);
        tilRepeatPassword.setError(null);

        if (!ValidateHelper.validateNecessaryString(edtPassword.getEditableText())) {
            tilPassword.setError(ValidateHelper.createMustFillError(this, getString(R.string.password)));
            return false;
        }

        if (!ValidateHelper.validateNecessaryString(edtRepeatPassword.getEditableText())) {
            tilRepeatPassword.setError(ValidateHelper.createMustFillError(this, getString(R.string.repeat_password)));
            return false;
        }

        if (edtPassword.getText().toString().length() < 6) {
            tilPassword.setError("حد اقل 6 رقم");
            return false;
        }

        pass = edtPassword.getText().toString();
        repeatPass = edtRepeatPassword.getText().toString();

        if (pass.contains(" ")) {

            new CustomDialogBuilder().showAlert(CreateUserActivity.this, "استفاده از فاصله در کلمه عبور نامعتبر میباشد");
            return false;
        }

        if (pass.equals(repeatPass)) {
            return true;
        }

        new CustomDialogBuilder().showAlert(CreateUserActivity.this, getString(R.string.error_pass_repeatPass));
        return false;


    }


    private void login() {
        if (repo == null)
            return;

        if (user == null)
            return;

        lyProgress.setVisibility(View.VISIBLE);

        repo.login(CreateUserActivity.this, user, new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {
                lyProgress.setVisibility(View.GONE);


                if (answer == null) {
                    new CustomDialogBuilder().showAlert(CreateUserActivity.this, getString(R.string.error_login_please_try_again));
                    return;
                }

                if (TextUtils.isEmpty(answer.getToken())) {
                    new CustomDialogBuilder().showAlert(CreateUserActivity.this, getString(R.string.error_login_please_try_again));
                    return;
                }

                user.setToken(answer.getToken());
                user.setTokenType(answer.getTokenType());
                user.setTokenExpireDate(answer.getTokenExpireDate());

// TODO: 5/31/2019 TOKEN IS BASE 64. MUST GET User FirstName and LastName From Token
                if (SessionManagement.getInstance(getApplicationContext()).saveMemberData(CreateUserActivity.this, user)) {

                    setResult(RESULT_OK);
                    MainActivity.start(CreateUserActivity.this);
                    finish();

                }


            }

            @Override
            public void onError(Throwable error) {
                new CustomDialogBuilder().showAlert(CreateUserActivity.this, error.getMessage().toString());
                lyProgress.setVisibility(View.GONE);
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
