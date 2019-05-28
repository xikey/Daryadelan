package daryadelan.sandogh.zikey.com.daryadelan.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private setOnSmsReciever smsReciever;

    public SMSBroadcastReceiver() {

    }


    public SMSBroadcastReceiver(setOnSmsReciever smsReciever) {
        this.smsReciever = smsReciever;
    }

    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr += smsBody ;

                if (smsReciever != null)
                    smsReciever.onSmsReciever(smsMessageStr);
            }

        }
    }

    public static void registerBroadCast(Context context, SMSBroadcastReceiver receiver) {

        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            context.registerReceiver(receiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void unRegisterBroadCast(Context context, SMSBroadcastReceiver receiver) {

        try {
            context.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface setOnSmsReciever {
        void onSmsReciever(String sms);
    }

}
