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

    private final String KEY_MOBILE = "KEY_MOBILE";
    private final String KEY_TOKEN = "TOKEN";
    private final String KEY_TOKEN_TYPE = "TOKEN_TYPE";
    private final String KEY_TOKEN_EXPIRE_DATE = "TOKEN_EXPIRE_DATE";
    private final String KEY_FIRST_NAME = "FIRST_NAME";
    private final String KEY_LAST_NAME = "LAST_NAME";
    private final String KEY_PERSONEL_CODE = "PERSONEL_CODE";


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

    /**
     * یکی از موارد زیر که نوع کاربر می باشد را بر میگرداند:
     * Baz
     * Vaz
     * Mos
     * Su
     * guest
     * مورد baz برای بازنشسته
     * مورد vaz برای وظیفه بگیر
     * مورد mos برای مستمری بگیر سازمانی
     * مورد su برای کاربر سوپر یوزر
     * مورد guest برای کاربر مهمان
     */
    private final String KEY_USER_TYPE = "USER_TYPE";


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

    public void setToken(String token) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_TOKEN, token).commit();

    }

    public String getToken() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_TOKEN, "");
    }


    public void setPersonType(String token) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_USER_TYPE, token).commit();

    }

    public String getPersonType() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_USER_TYPE, "");
    }


    public void setToken_type(String tokenType) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_TOKEN_TYPE, tokenType).commit();

    }

    public long getPersonelCode() {
        if (preferences == null)
            return 0;
        return preferences.getLong(KEY_PERSONEL_CODE, 0);
    }

    public void setPersonelCode(long personelCode) {
        if (preferences == null)
            return;
        this.preferences.edit().putLong(KEY_PERSONEL_CODE, personelCode).commit();

    }


    public String getToken_type() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_TOKEN_TYPE, "");
    }

    public void setToken_ExpireDate(String tokenExpireDate) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_TOKEN_EXPIRE_DATE, tokenExpireDate).commit();

    }

    public String getToken__ExpireDate() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_TOKEN_EXPIRE_DATE, "");
    }

    public void setTel(String tel) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_MOBILE, tel).commit();

    }

    public String getTel() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_MOBILE, "");
    }


    public void setFirstName(String tel) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_FIRST_NAME, tel).commit();

    }

    public String getFirstName() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_FIRST_NAME, "");
    }

    public void setLastName(String tel) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_LAST_NAME, tel).commit();

    }

    public String getLastName() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_LAST_NAME, "");
    }


    public void clearMemberData() {
        setToken("");
        setTel("");
        setFirstName("");
        setLastName("");
        setPersonelCode(0);
        setPersonType("");


    }


    public boolean saveMemberData(AppCompatActivity context, User member) {
        if (member == null)
            return false;

        DialogBuilder dialog = new DialogBuilder();

        if (TextUtils.isEmpty(member.getToken())) {
            dialog.showAlert(context, context.getString(R.string.server_token_eror));
            return false;
        }
        if (TextUtils.isEmpty(member.getMobile())) {
            dialog.showAlert(context, context.getString(R.string.server_mobile_eror));
            return false;
        }

//        if (TextUtils.isEmpty(member.getFirstName())) {
//            dialog.showAlert(context, context.getString(R.string.server_firstName_eror));
//            return false;
//        }
//
//        if (TextUtils.isEmpty(member.getLastName())) {
//            dialog.showAlert(context, context.getString(R.string.server_lastName_eror));
//            return false;
//        }


        setToken(member.getToken());
        setTel(member.getMobile());
        setFirstName(member.getFirstName());
        setLastName(member.getLastName());

        try {
            setPersonelCode(member.getPersonalCode());
            setPersonType(member.getPersonType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(member.getTokenExpireDate())) {
            setToken_ExpireDate(member.getTokenExpireDate());
        }

        if (!TextUtils.isEmpty(member.getTokenType())) {
            setToken_type(member.getTokenType());
        }
        return true;

    }

    public boolean saveMemberExtraInfo(AppCompatActivity context, User member) {
        if (member == null)
            return false;


        setTel(member.getMobile());
        setFirstName(member.getFirstName());
        setLastName(member.getLastName());
        setPersonelCode(member.getPersonalCode());
        setPersonType(member.getPersonType());

        if (!TextUtils.isEmpty(member.getTokenExpireDate())) {
            setToken_ExpireDate(member.getTokenExpireDate());
        }

        if (!TextUtils.isEmpty(member.getTokenType())) {
            setToken_type(member.getTokenType());
        }
        return true;

    }


    public User loadMember() {

        User member = new User();
        if (TextUtils.isEmpty(getToken()))
            return null;
        else if (TextUtils.isEmpty(getTel()))
            return null;
//        else if (TextUtils.isEmpty(getFirstName()))
//            return null;
//        else if (TextUtils.isEmpty(getLastName()))
//            return null;

        member.setToken(getToken());
        member.setFirstName(getFirstName());
        member.setLastName(getLastName());
        member.setToken(getToken());
        member.setMobile(getTel());
        member.setTokenExpireDate(getToken__ExpireDate());
        member.setTokenType(getToken_type());
        member.setPersonalCode(getPersonelCode());
        member.setPersonType(getPersonType());

        return member;

    }

    public void clearSession(Context context) {
        if (preferences == null)
            return;


        SharedPreferences.Editor pref = context.getSharedPreferences(KEY_PREFERENCES_NAME + BuildConfig.VERSION_CODE, Context.MODE_PRIVATE).edit();
        pref.clear();
        pref.apply();

        instance = null;


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
