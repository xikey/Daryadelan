package daryadelan.sandogh.zikey.com.daryadelan;

import android.Manifest;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import daryadelan.sandogh.zikey.com.daryadelan.broadcasts.SMSBroadcastReceiver;
import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.TestRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.UserServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.DeviceHelper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import es.dmoral.toasty.Toasty;

public class SigninActivity extends AppCompatActivity {

    private final int RESOLVE_HINT = 45;
    private TextView txtSendSMS;
    GoogleApiClient apiClient;
    private SMSBroadcastReceiver SMSBroadcastReceiver;
    public final int PERMISSIONS_REQUEST_READ_SMS = 14;
    public final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 16;

    private EditText txtUserName;
    private EditText txtPersonelCode;

    private ImageView imgMySim;

    private LinearLayout lyLogin;
    private LinearLayout lyGuest;
    private RelativeLayout lyHeader;
    private LinearLayout lyProgress;
    private User personel;

    private IUser userRepo;
    private SMSvalidationDialog smSvalidationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initViews();
        initListeners();
        initRepo();
        hideKeyBoard();
        initApiClient();
        initBroadCast();
        requestGetMessagePermission();
        requestReadPhoneStatePermission();

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
                                if (smSvalidationDialog != null) {
                                    smSvalidationDialog.userInputDialog.setText(validateCode);
                                }
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

        initHeaderSize();
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
        if (txtSendSMS != null)
            txtSendSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CustomDialogBuilder().showYesNOCustomAlert(SigninActivity.this, "ارسال اطلاعات", "مایل به ارسال اطلاعات میباشید؟", "ارسال", "انصراف", new CustomAlertDialog.OnActionClickListener() {
                        @Override
                        public void onClick(DialogFragment fragment) {

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
                LoginActivity.start(SigninActivity.this);

            }
        });

        lyGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginGuestActivity.start(SigninActivity.this);

            }
        });
    }

    private void sendSMS() {


        if (TextUtils.isEmpty(txtUserName.getText())) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "شماره موبایل وارد شده نادرست میباشد");
            return;
        }
        if (TextUtils.isEmpty(txtPersonelCode.getText())) {
            new CustomDialogBuilder().showAlert(SigninActivity.this, "شماره پرسنلی وارد شده نادرست میباشد");
            return;
        }


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        if (permission != 0) {
            requestReadPhoneStatePermission();
            return;
        }

        DeviceHelper deviceHelper = new DeviceHelper();
        final User user = new User();

        user.setMobile(txtUserName.getText().toString());
        user.setPersonalCode(Long.parseLong(txtPersonelCode.getText().toString()));

        user.setMobileImei(deviceHelper.getMobileIMEI(getApplicationContext()));
        user.setMobileDeviceBrand(deviceHelper.getDeviceName(getApplicationContext()));
        user.setPersonType("baz");
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
                personel = user;
                String tmp = "کد ارسالی به شماره موبایل " + user.getMobile() + " را وارد نمایید ";
                smSvalidationDialog = SMSvalidationDialog.Show(SigninActivity.this, getString(R.string.sms_validate), tmp, "تایید", "انصراف", new SMSvalidationDialog.OnActionClickListener() {
                    @Override
                    public void onClick(DialogFragment fragment, String input) {
                        if (TextUtils.isEmpty(input)) {
                            new CustomDialogBuilder().showAlert(SigninActivity.this, getString(R.string.error_Verification_code));
                            return;
                        }
                        fragment.dismiss();
                        checkActivateCodeValidation();

                    }
                }, new SMSvalidationDialog.OnCancelClickListener() {
                    @Override
                    public void onClickCancel(DialogFragment fragment, String input, boolean isPleaseWait) {

                        if (!isPleaseWait) {
                            fragment.dismiss();
                            sendSMS();

                        } else
                            fragment.dismiss();

                    }

                    @Override
                    public void onClickOutside(DialogFragment fragment, String input) {
                        fragment.dismiss();
                    }
                }, new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            personel.setActiveCode(s.toString());
                        }
                    }
                });


            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                new CustomDialogBuilder().showAlert(SigninActivity.this, error.toString());
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

        txtSendSMS = (TextView) findViewById(R.id.txtSendSMS);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPersonelCode = (EditText) findViewById(R.id.txtPersonelCode);
        imgMySim = (ImageView) findViewById(R.id.imgMySim);
        lyHeader = (RelativeLayout) findViewById(R.id.lyHeader);

        lyLogin = (LinearLayout) findViewById(R.id.lyLogin);
        lyGuest = (LinearLayout) findViewById(R.id.lyGuest);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);


        try {
            FontChanger.applyMainFont(findViewById(R.id.lyRoot));
            FontChanger.applyTitleFont(txtSendSMS);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    }

//    private void startSMSretriever() {
//
//        // Get an instance of SmsRetrieverClient, used to start listening for a matching
//// SMS message.
//        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);
//
//// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
//// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
//// action SmsRetriever#SMS_RETRIEVED_ACTION.
//        Task<Void> task = client.startSmsRetriever();
//
//// Listen for success/failure of the start Task. If in a background thread, this
//// can be made blocking using Tasks.await(task, [timeout]);
//        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toasty.success(SigninActivity.this, "Successfully").show();
//                MySMSBroadcastReceiver.registerBroadCast(SigninActivity.this, smsBroadcastReceiver);
//                // Successfully started retriever, expect broadcast intent
//                // ...
//            }
//        });
//
//        task.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toasty.success(SigninActivity.this, "Failed").show();
//                // Failed to start retriever, inspect Exception for more details
//                // ...
//            }
//        });
//
//    }

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
            }
            case PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    // if Permission Denied
                }
            }

        }
    }


    private void initHeaderSize() {

        int width = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = lyHeader.getLayoutParams();
        int height = (width / 2);
        params.height = height;
        lyHeader.setLayoutParams(params);

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

                CreateUserActivity.start(SigninActivity.this, answer.getStrData());

            }

            @Override
            public void onError(Throwable error) {
                lyProgress.setVisibility(View.GONE);
                new CustomDialogBuilder().showAlert(SigninActivity.this, error.toString());
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

    private void testApi() {

        TestRepo testRepo = new TestRepo();
        testRepo.testing(getApplicationContext());
    }

}
