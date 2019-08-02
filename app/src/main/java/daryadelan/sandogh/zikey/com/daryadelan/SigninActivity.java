package daryadelan.sandogh.zikey.com.daryadelan;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import daryadelan.sandogh.zikey.com.daryadelan.broadcasts.SMSBroadcastReceiver;
import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.UserServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.sqlite.UserSqliteRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.DeviceHelper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import es.dmoral.toasty.Toasty;

public class SigninActivity extends AppCompatActivity {


    private static final int KEY_REQUEST_LOGIN = 11;
    private static final int KEY_REQUEST_CREATE_USER = 13;


    private final int RESOLVE_HINT = 45;
    private CardView lySendSMS;
    GoogleApiClient apiClient;
    private SMSBroadcastReceiver SMSBroadcastReceiver;
    public final int PERMISSIONS_REQUEST_READ_SMS = 14;
    public final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 16;

    private EditText txtUserName;
    private EditText txtPersonelCode;

    private ImageView imgMySim;

    private CardView lyLogin;
    //    private LinearLayout lyGuest;
    private RelativeLayout lyHeader;
    private LinearLayout lyProgress;
    private CardView lyPersonelCode;
    private User personel;

    private IUser userRepo;
    private SMSvalidationDialog smSvalidationDialog;
    private SMSvalidationDialogFullScreen smSvalidationDialogFullScreen;

    private IUser userSqliteRepo;

    private AnimatorSet shirinkLogo;
    private AnimatorSet globalAnimate;
    private CardView crdContainer;

    private boolean isGuest = false;

//    private Switch swIAmGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestGetMessagePermission();
        initRepo();
        isUserLoggedInBefore();
        initApiClient();
        initBroadCast();


    }

    /**
     * در صورتی که کاربر قبلا ورود کرده باشد دیگر نباید بتواند صفحه ساخت یوزر را ببیند
     */
    private void isUserLoggedInBefore() {

        userSqliteRepo.getUserDatas(getApplicationContext(), new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {
                if (answer != null && !TextUtils.isEmpty(answer.getToken())) {
                    MainActivity.start(SigninActivity.this);
                    finish();
                } else {

                    if (Build.VERSION.SDK_INT < 16) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    } else {
                        View decorView = getWindow().getDecorView();
// Hide the status bar.
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);


                    }


                    if (Build.VERSION.SDK_INT < 16) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    } else {
                        View decorView = getWindow().getDecorView();
// Hide the status bar.
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);


                    }
                    setContentView(R.layout.activity_signin_2);
                    initViews();
                    initHeaderSize();
                    initListeners();
                    hideKeyBoard();
                    clearSession();

                }
            }

            @Override
            public void onError(Throwable error) {
                setContentView(R.layout.activity_signin_2);
                initViews();
                initHeaderSize();
                initListeners();
                hideKeyBoard();
                clearSession();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        if (SMSBroadcastReceiver != null)
            SMSBroadcastReceiver.unRegisterBroadCast(getApplicationContext(), SMSBroadcastReceiver);
        super.onDestroy();
    }

    private void initRepo() {
        if (userRepo == null)
            userRepo = new UserServerRepo();

        if (userSqliteRepo == null)
            userSqliteRepo = new UserSqliteRepo();

    }

    private void initBroadCast() {

        if (SMSBroadcastReceiver == null)
            SMSBroadcastReceiver = new SMSBroadcastReceiver(new SMSBroadcastReceiver.setOnSmsReciever() {
                @Override
                public void onSmsReciever(String sms) {
                    if (!TextUtils.isEmpty(sms) && personel != null) {
                        try {
                            String validateCode = "";
                            validateCode += sms.replaceAll("[^0-9]", "");
                            if (!TextUtils.isEmpty(validateCode)) {

                                {
                                    if (smSvalidationDialogFullScreen != null)
                                        smSvalidationDialogFullScreen.fillFromSMS(validateCode);
                                    checkActivateCodeValidation();
                                    if (smSvalidationDialog != null)
                                        smSvalidationDialog.dismiss();
                                }

//                                if (smSvalidationDialog != null) {
//                                    smSvalidationDialog.userInputDialog.setText(validateCode);
//                                    checkActivateCodeValidation();
//                                    if (smSvalidationDialog != null)
//                                        smSvalidationDialog.dismiss();
//                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        animateLogo();

        SMSBroadcastReceiver.registerBroadCast(getApplicationContext(), SMSBroadcastReceiver);

    }

    private void initApiClient() {

        apiClient = new GoogleApiClient.Builder(getApplicationContext(), new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        }, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).enableAutoManage(this /* FragmentActivity */,
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.CREDENTIALS_API)
                .build();
    }

    private void hideKeyBoard() {
        try {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initListeners() {
        if (lySendSMS != null)
            lySendSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CustomDialogBuilder().showYesNOCustomAlert(SigninActivity.this, "ارسال اطلاعات", "مایل به ارسال اطلاعات میباشید؟", "ارسال", "انصراف", new CustomAlertDialog.OnActionClickListener() {
                        @Override
                        public void onClick(DialogFragment fragment) {
                            isGuest = false;
                            fragment.dismiss();
                            sendSMS();

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
            });

        imgMySim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    requestHint();
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        lyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(SigninActivity.this, KEY_REQUEST_LOGIN);

            }
        });

//        swIAmGuest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    isGuest = true;
//                    lyPersonelCode.setVisibility(View.GONE);
//
//                } else {
//                    isGuest = false;
//                    lyPersonelCode.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    private void sendSMS() {


        if (TextUtils.isEmpty(txtUserName.getText())) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "شماره موبایل وارد شده نادرست میباشد");
            return;
        }


        if (TextUtils.isEmpty(txtPersonelCode.getText())) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "کد ملی وارد شده نادرست میباشد");
            return;
        }

        if (!isValidNationalCode(txtPersonelCode.getText().toString()))
            return;


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        if (permission != 0) {
            requestReadPhoneStatePermission();
            return;
        }

        DeviceHelper deviceHelper = new DeviceHelper();
        final User user = new User();

        user.setMobile(txtUserName.getText().toString());

//به این دلیل مقدار کد ملی از مقدار کد پرسنلی دریافت میشود که سمت درخاست کاربران عوض شد ولی سمت سرور تغییر نکرد!!
        user.setPersonalCode(Long.parseLong((txtPersonelCode.getText().toString())));
        user.setMobileImei(deviceHelper.getMobileIMEI(getApplicationContext()));
        user.setMobileDeviceBrand(deviceHelper.getDeviceName(getApplicationContext()));
        user.setOsVersion(deviceHelper.getOSversion(getApplicationContext()));

        if (TextUtils.isEmpty(user.getMobileImei())) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "گوشی هوشمند نامعتبر!\nدر صورتی که از شبیه ساز یا دستگاه نامعتبر استفاده نمایید، امکان ورود به نرم افزار نخواهد بود!");
            return;
        }
        if (TextUtils.isEmpty(user.getMobileDeviceBrand())) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "گوشی هوشمند نامعتبر!\nدر صورتی که از شبیه ساز یا دستگاه نامعتبر استفاده نمایید، امکان ورود به نرم افزار نخواهد بود!");
            return;
        }
        if (TextUtils.isEmpty(user.getOsVersion())) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "گوشی هوشمند نامعتبر!\nدر صورتی که از شبیه ساز یا دستگاه نامعتبر استفاده نمایید، امکان ورود به نرم افزار نخواهد بود!");
            return;
        }


        lyProgress.setVisibility(View.VISIBLE);
        userRepo.personCheck(getApplicationContext(), user, new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {
                lyProgress.setVisibility(View.GONE);
                if (answer.getResultId() != 0) {
                    return;
                }
                Toasty.success(SigninActivity.this, answer.getMessagee()).show();
                if (TextUtils.isEmpty(answer.getStrData())) {
                    new CustomDialogBuilder().showAlert(SigninActivity.this, "متاسفانه کد پرسنلی و شماره همراه شما مجوز ورود به نرم افزار ندارد. در صورت مشکل با واحد پشتیبانی تماس حاصل نمایید");
                    return;
                }
                user.setPersonType(answer.getStrData());
                personel = user;
                String tmp = "کد ارسالی به شماره موبایل " + user.getMobile() + " را وارد نمایید ";

                smSvalidationDialogFullScreen=SMSvalidationDialogFullScreen.Show(SigninActivity.this, new SMSvalidationDialogFullScreen.IInputTextWatcher() {
                    @Override
                    public void onDone(String input) {
                       if (smSvalidationDialogFullScreen!=null)
                           smSvalidationDialogFullScreen.dismiss();

                       try {
                           personel.setActiveCode(input);
                       }catch (Exception e){
                           e.printStackTrace();
                       }

                        checkActivateCodeValidation();

                    }
                });

//                smSvalidationDialog = SMSvalidationDialog.Show(SigninActivity.this, getString(R.string.sms_validate), tmp, "تایید", "انصراف", new SMSvalidationDialog.OnActionClickListener() {
//                    @Override
//                    public void onClick(DialogFragment fragment, String input) {
//                        if (TextUtils.isEmpty(input)) {
//                            new CustomDialogBuilder().showAlert(SigninActivity.this, getString(R.string.error_Verification_code));
//                            return;
//                        }
//                        fragment.dismiss();
//                        checkActivateCodeValidation();
//
//                    }
//                }, new SMSvalidationDialog.OnCancelClickListener() {
//                    @Override
//                    public void onClickCancel(DialogFragment fragment, String input, boolean isPleaseWait) {
//
//                        if (!isPleaseWait) {
//                            fragment.dismiss();
//                            sendSMS();
//
//                        } else
//                            fragment.dismiss();
//
//                    }
//
//                    @Override
//                    public void onClickOutside(DialogFragment fragment, String input) {
//                        fragment.dismiss();
//                    }
//                }, new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        if (!TextUtils.isEmpty(s.toString())) {
//                            personel.setActiveCode(s.toString());
//                        }
//                    }
//                });


            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                new CustomDialogBuilder().showAlert(SigninActivity.this, error.getMessage());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });


    }


    public static void start(Context context) {
        Intent starter = new Intent(context, SigninActivity.class);
        context.startActivity(starter);
    }

    private void initViews() {

        lySendSMS = (CardView) findViewById(R.id.lySendSMS);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPersonelCode = (EditText) findViewById(R.id.txtPersonelCode);
        imgMySim = (ImageView) findViewById(R.id.imgMySim);
        lyHeader = (RelativeLayout) findViewById(R.id.lyHeader);

        lyLogin = (CardView) findViewById(R.id.lyLogin);
//        lyGuest = (LinearLayout) findViewById(R.id.lyGuest);
        lyPersonelCode = (CardView) findViewById(R.id.lyPersonelCode);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        crdContainer = (CardView) findViewById(R.id.crdContainer);
//        swIAmGuest = (Switch) findViewById(R.id.swIAmGuest);


        try {
            FontChanger.applyMainFont(findViewById(R.id.lyRoot));
            FontChanger.applyTitleFont(lySendSMS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        animateLogo();
    }

    private void requestHint() throws IntentSender.SendIntentException {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                apiClient, hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                RESOLVE_HINT, null, 0, 0, 0);
    }

    //  Obtain the phone number from the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (txtUserName != null) {
                    String number = credential.getId();
                    if (!TextUtils.isEmpty(number)) {
                        if (number.contains("+98"))
                            number = number.replace("+98", "0");
                        txtUserName.setText(number);
                    }

                }

            }
        }
        // در صورتی که عملیات لاگین با موفقیت اتجام شود
        if (requestCode == KEY_REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {

                finish();
            }
        }

        if (requestCode == KEY_REQUEST_CREATE_USER) {
            if (resultCode == RESULT_OK) {

                finish();
            }
        }
    }


    private void requestGetMessagePermission() {

        int smsPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);


        if (smsPermission == 0)
            return;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECEIVE_SMS)) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS},
                        PERMISSIONS_REQUEST_READ_SMS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS},
                        PERMISSIONS_REQUEST_READ_SMS);
            }
        }

    }


    private void requestReadPhoneStatePermission() {

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);


        if (permission == 0)
            return;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_READ_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // if Permission Denied
                }
                break;
            }
            case PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    // if Permission Denied
                }
                break;
            }

        }
    }


    private void initHeaderSize() {


        try {
            int width = getResources().getDisplayMetrics().widthPixels;
            ViewGroup.LayoutParams params = lyHeader.getLayoutParams();
            int height = ((width));
            params.height = height;
            lyHeader.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int widthPixels = metrics.widthPixels;
            int heightPixels = metrics.heightPixels;
            float scaleFactor = metrics.density;
            float widthDp = widthPixels / scaleFactor;
            float heightDp = heightPixels / scaleFactor;
            float smallestWidth = Math.min(widthDp, heightDp);

            // is Tablet?
            if (smallestWidth > 600) {
                int height = getResources().getDisplayMetrics().heightPixels;
                int width = getResources().getDisplayMetrics().widthPixels;
                ViewGroup.LayoutParams params = crdContainer.getLayoutParams();
                params.height = height / 2;
                crdContainer.setLayoutParams(params);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void checkActivateCodeValidation() {
        if (userRepo == null)
            return;

        if (TextUtils.isEmpty(personel.getActiveCode())) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "کد فعال سازی نامعتبر میباشد");
            return;
        }
        lyProgress.setVisibility(View.VISIBLE);
        userRepo.checkSMSisValidate(getApplicationContext(), personel, new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {
                lyProgress.setVisibility(View.GONE);

                if (answer.getResultId() != 0) {
                    new CustomDialogBuilder().showAlert(SigninActivity.this, "کد فعال سازی نامعتبر میباشد");
                    return;
                }

                if (TextUtils.isEmpty(answer.getStrData())) {
                    new CustomDialogBuilder().showAlert(SigninActivity.this, "کد فعال سازی نامعتبر میباشد");
                    return;
                }
                if (TextUtils.isEmpty(answer.getStrData())) {
                    new CustomDialogBuilder().showAlert(SigninActivity.this, "کد فعال سازی نامعتبر میباشد");
                    return;
                }
                if (!TextUtils.isEmpty(answer.getMessagee())) {

                    Toasty.info(SigninActivity.this, answer.getMessagee()).show();
                }

                personel.setAcceptCode(answer.getStrData());

                String firstName = null;
                String lastname = null;

                try {
                    if (answer.getUserExtrasInfo() != null && answer.getUserExtrasInfo().size() != 0) {
                        firstName = answer.getUserExtrasInfo().get(0).getFirstName();
                        lastname = answer.getUserExtrasInfo().get(0).getLastName();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                CreateUserActivity.start(SigninActivity.this, personel.getAcceptCode(), personel.getPersonType(), personel.getMobile(), firstName, lastname, KEY_REQUEST_CREATE_USER);

            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                new CustomDialogBuilder().showAlert(SigninActivity.this, error.getMessage());
                return;
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });

    }


    public Boolean isValidNationalCode(String nationalCode) {
        //در صورتی که کد ملی وارد شده تهی باشد

        if (TextUtils.isEmpty(nationalCode)) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "مقدار کد ملی نمیتواند خالی باشد");
            return false;
        }

        //در صورتی که کد ملی وارد شده طولش کمتر از 10 رقم باشد
        if (nationalCode.length() != 10) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "مقدار کد ملی باید ده رقم باشد");
            return false;
        }

        //در صورتی که کد ملی ده رقم عددی نباشد
        if (!TextUtils.isDigitsOnly(nationalCode)) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "مقدار کد ملی نمیتواند شامل حروف باشد");
            return false;
        }

        //در صورتی که رقم‌های کد ملی وارد شده یکسان باشد
        String allDigitEqual[] = new String[]{
                "0000000000", "1111111111", "2222222222", "3333333333", "4444444444", "5555555555", "6666666666", "7777777777", "8888888888", "9999999999"
        };

        for (int i = 0; i < allDigitEqual.length; i++) {
            if (allDigitEqual[i].equals(nationalCode)) {
                new CustomDialogBuilder().showAlert(SigninActivity.this, "مقدار کد ملی نادرست میباشد");
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
                new CustomDialogBuilder().showAlert(SigninActivity.this, "مقدار کد ملی نادرست میباشد");
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            new CustomDialogBuilder().showAlert(SigninActivity.this, "مقدار کد ملی نادرست میباشد");
            return false;
        }

    }

    private void clearSession() {

        if (userSqliteRepo != null)
            userSqliteRepo.exitApp(getApplicationContext(), new IRepoCallBack<User>() {
                @Override
                public void onAnswer(User answer) {

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

    private void animateLogo() {
        if (crdContainer == null)
            return;

        shirinkLogo = new AnimatorSet();
//        Animator animatorShirinkX = ObjectAnimator.ofFloat(crdContainer, "scaleX", 1f, 0.9f);
//        animatorShirinkX.setDuration(50);
//        animatorShirinkX.setInterpolator(new DecelerateInterpolator());
//
//        Animator animatorShirinkY = ObjectAnimator.ofFloat(crdContainer, "scaleY", 1f, 0.9f);
//        animatorShirinkY.setDuration(50);
//        animatorShirinkY.setInterpolator(new DecelerateInterpolator());
//
//        shirinkLogo.playTogether(animatorShirinkX, animatorShirinkY);


        AnimatorSet setGrow = new AnimatorSet();
        Animator animatorGrowX = ObjectAnimator.ofFloat(crdContainer, "scaleX", 0.9f, 1.1f);
        animatorGrowX.setDuration(200);
        animatorGrowX.setInterpolator(new AccelerateInterpolator());

        Animator animatorGrowY = ObjectAnimator.ofFloat(crdContainer, "scaleY", 0.9f, 1.1f);
        animatorGrowY.setDuration(200);
        animatorGrowY.setInterpolator(new AccelerateInterpolator());

        setGrow.playTogether(animatorGrowX, animatorGrowY);


        AnimatorSet setFix = new AnimatorSet();
        Animator setFixX = ObjectAnimator.ofFloat(crdContainer, "scaleX", 1.1f, 1f);
        setFixX.setDuration(800);
        setFixX.setInterpolator(new BounceInterpolator());

        Animator setFixY = ObjectAnimator.ofFloat(crdContainer, "scaleY", 1.1f, 1f);
        setFixY.setDuration(800);
        setFixY.setInterpolator(new BounceInterpolator());
        setFix.playTogether(setFixX, setFixY);

        globalAnimate = new AnimatorSet();
        globalAnimate.playSequentially(shirinkLogo, setGrow, setFix);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(globalAnimate);
        animatorSet.setStartDelay(50);
        animatorSet.start();


    }



}
