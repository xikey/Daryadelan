package daryadelan.sandogh.zikey.com.daryadelan.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import daryadelan.sandogh.zikey.com.daryadelan.BuildConfig;


/**
 * Created by Zikey on 11/01/2017.
 */

public class SessionManagement {
    // preference name
    private final String KEY_PREFERENCES_NAME = "DARYADELAN_APPLICATION";

    private final String KEY_MOBILE = "KEY_MOBILE";
    private final String KEY_TOKEN = "TOKEN";
    private final String KEY_STR_DATA = "PASS";



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






    public void clearMemberData() {
        setToken("");


    }


    public boolean saveMemberData(AppCompatActivity context) {
//        if (member == null)
//            return false;
//
//        DialogBuilder dialog = new DialogBuilder();
//
//        if (member.getCode_markaz() == 0) {
//            dialog.showAlert(context, context.getString(R.string.server_code_markaz_eror));
//            return false;
//        }
//        if (member.getToken() == null) {
//            dialog.showAlert(context, context.getString(R.string.server_token_eror));
//            return false;
//        }
//        if (member.getName() == null) {
//            dialog.showAlert(context, context.getString(R.string.server_name_eror));
//            return false;
//        }
////        if (member.getId() == 0) {
////            dialog.showAlert(context, context.getString(R.string.server_id_eror));
////            return false;
////        }
//
//        setCodeMarkaz(member.getCode_markaz());
//        setName(member.getName());
////        setID(member.getId());
//        setToken(member.getToken());
//        setTel(member.getTel());
//        setNavigationIP(member.getNavigationIpAddress());

        return true;

    }

    public void loadMember() {

//        Member member = new Member();
//        if (getCodeMarkaz() == 0)
//            return null;
//        else if (TextUtils.isEmpty(getName()))
//            return null;
//        else if (
//                TextUtils.isEmpty(getToken()))
//            return null;
//
//        member.setCode_markaz(getCodeMarkaz());
//        member.setName(getName());
//        member.setId(getID());
//        member.setToken(getToken());
//        member.setTel(getTel());
//
//        return member;

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
