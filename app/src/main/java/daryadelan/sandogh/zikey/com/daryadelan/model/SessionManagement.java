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
    private final String KEY_FIRST_NAME = "FIRST_NAME";
    private final String KEY_LAST_NAME = "LAST_NAME";


    private SharedPreferences preferences;
    public static SessionManagement instance = null;

    public SessionManagement(Context context) {
        this.preferences = context.getSharedPreferences(KEY_PREFERENCES_NAME + BuildConfig.FLAVOR, Context.MODE_PRIVATE);
    }

    public static SessionManagement getInstance(Context context) {

        if (instance == null)
            instance = new SessionManagement(context);
        return instance;
    }

    public void setToken(String token) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_TOKEN, token).apply();

    }

    public String getToken() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_TOKEN, "");
    }


    public void setTel(String tel) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_MOBILE, tel).apply();

    }

    public String getTel() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_MOBILE, "");
    }


    public void setFirstName(String tel) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_FIRST_NAME, tel).apply();

    }

    public String getFirstName() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_FIRST_NAME, "");
    }

    public void setLastName(String tel) {
        if (preferences == null)
            return;
        this.preferences.edit().putString(KEY_LAST_NAME, tel).apply();

    }

    public String getLastName() {
        if (preferences == null)
            return "";
        return preferences.getString(KEY_LAST_NAME, "");
    }


    public void clearMemberData() {
        setToken("");


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

        return member;

    }

    public void clearSession(Context context) {
        if (preferences == null)
            return;


        SharedPreferences.Editor pref = context.getSharedPreferences(KEY_PREFERENCES_NAME + BuildConfig.FLAVOR, Context.MODE_PRIVATE).edit();
        pref.clear();
        pref.apply();

        preferences = null;
    }


}
