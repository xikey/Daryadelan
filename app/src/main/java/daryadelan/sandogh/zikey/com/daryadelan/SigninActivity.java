package daryadelan.sandogh.zikey.com.daryadelan;

import android.Manifest;
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
import android.text.TextUtils;
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
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import es.dmoral.toasty.Toasty;

public class SigninActivity extends AppCompatActivity {

    private final int RESOLVE_HINT = 45;
    private TextView txtSendSMS;
    GoogleApiClient apiClient;
    private SMSBroadcastReceiver SMSBroadcastReceiver;
    public final int PERMISSIONS_REQUEST_READ_SMS = 14;

    private EditText txtUserName;
    private ImageView imgMySim;

    private LinearLayout lyLogin;
    private LinearLayout lyGuest;
    private RelativeLayout lyHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initViews();
        initListeners();
        hideKeyBoard();
        initApiClient();
        initBroadCast();
        requestGetMessagePermission();
    }

    private void initBroadCast() {

//        if (smsBroadcastReceiver == null)
//            smsBroadcastReceiver = new MySMSBroadcastReceiver(new MySMSBroadcastReceiver.setOnSmsReciever() {
//                @Override
//                public void onSmsReciever(String sms) {
//
//                    if (!TextUtils.isEmpty(sms))
//                        Toasty.info(SigninActivity.this, sms).show();
//                }
//            });

        if (SMSBroadcastReceiver == null)
            SMSBroadcastReceiver = new SMSBroadcastReceiver(new SMSBroadcastReceiver.setOnSmsReciever() {
                @Override
                public void onSmsReciever(String sms) {
                    if (!TextUtils.isEmpty(sms))
                        Toasty.info(SigninActivity.this, "ZIKEY  " + sms).show();
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

                    MainActivity.start(SigninActivity.this);

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


    public static void start(Context context) {
        Intent starter = new Intent(context, SigninActivity.class);
        context.startActivity(starter);
    }

    private void initViews() {

        txtSendSMS = (TextView) findViewById(R.id.txtSendSMS);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        imgMySim = (ImageView) findViewById(R.id.imgMySim);
        lyHeader = (RelativeLayout) findViewById(R.id.lyHeader);

        lyLogin = (LinearLayout) findViewById(R.id.lyLogin);
        lyGuest = (LinearLayout) findViewById(R.id.lyGuest);


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_READ_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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


}
