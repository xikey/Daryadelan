package daryadelan.sandogh.zikey.com.daryadelan.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.razanpardazesh.razanlibs.CustomView.DialogBuilder;

import daryadelan.sandogh.zikey.com.daryadelan.BuildConfig;
import daryadelan.sandogh.zikey.com.daryadelan.R;


/**
 * Created by Zikey on 11/01/2017.
 */

public class SessionManagement {
    // preference name
    private final String KEY_PREFERENCES_NAME = "DARYADELAN_APPLICATION";


    private final String KEY_ADVERTISE_1 = "ADVERTISE_1";
    private final String KEY_ADVERTISE_1_URI = "ADVERTISE_1_URI";
    private final String KEY_ADVERTISE_2 = "ADVERTISE_2";
    private final String KEY_ADVERTISE_2_URI = "ADVERTISE_2_URI";
    private final String KEY_ADVERTISE_3 = "ADVERTISE_3";
    private final String KEY_ADVERTISE_3_URI = "ADVERTISE_3_URI";
    private final String KEY_ADVERTISE_4 = "ADVERTISE_4";
    private final String KEY_ADVERTISE_4_URI = "ADVERTISE_4_URI";
    private final String KEY_ADVERTISE_5 = "ADVERTISE_5";
    private final String KEY_ADVERTISE_5_URI = "ADVERTISE_5_URI";



    private SharedPreferences preferences;
    public static SessionManagement instance = null;

    public SessionManagement(Context context) {
        this.preferences = context.getSharedPreferences(KEY_PREFERENCES_NAME + BuildConfig.VERSION_CODE, Context.MODE_PRIVATE);
    }

    public static SessionManagement getInstance(Context context) {

        if (instance == null)
            instance = new SessionManagement(context);
        return instance;
    }





    public String getAdvertise_1Url() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_1, "");
    }

    public String getAdvertise_2Url() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_2, "");
    }

    public String getAdvertise_3Url() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_3, "");
    }

    public String getAdvertise_4Url() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_4, "");
    }

    public String getAdvertise_5Url() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_5, "");
    }

    public String getAdvertise_1Uri() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_1_URI, "");
    }

    public String getAdvertise_2Uri() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_2_URI, "");
    }

    public String getAdvertise_3Uri() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_3_URI, "");
    }

    public String getAdvertise_4Uri() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_4_URI, "");
    }

    public String getAdvertise_5Uri() {

        if (this.preferences == null)
            return "";

        return this.preferences.getString(KEY_ADVERTISE_5_URI, "");
    }

    public void setAdvertise_5Url(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_5, url).commit();
    }

    public void setAdvertise_4Url(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_4, url).commit();
    }

    public void setAdvertise_3Url(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_3, url).commit();
    }

    public void setAdvertise_2Url(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_2, url).commit();
    }

    public void setAdvertise_1Url(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_1, url).commit();
    }

    public void setAdvertise_1Uri(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_1_URI, url).commit();
    }

    public void setAdvertise_2Uri(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_2_URI, url).commit();
    }

    public void setAdvertise_3Uri(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_3_URI, url).commit();
    }

    public void setAdvertise_4Uri(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_4_URI, url).commit();
    }

    public void setAdvertise_5Uri(String url) {
        if (this.preferences == null)
            return;

        this.preferences.edit().putString(KEY_ADVERTISE_5_URI, url).commit();
    }


}
