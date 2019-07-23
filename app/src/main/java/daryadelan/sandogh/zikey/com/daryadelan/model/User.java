package daryadelan.sandogh.zikey.com.daryadelan.model;

import android.database.Cursor;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.data.UserData;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;

public class User extends ServerWrapper {

    @SerializedName("personalCode")
    @Expose
    private long personalCode;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("mobileDeviceBrand")
    private String mobileDeviceBrand;
    @SerializedName("mobileImei")
    private String mobileImei;
    @SerializedName("osVersion")
    private String osVersion;
    /**
     * یکی از موارد زیر که نوع کاربر می باشد را بر میگرداند:
     * Baz
     * <p>
     * Vaz
     * Mos
     * Su
     * guest
     * employee
     * مورد baz برای بازنشسته
     * مورد vaz برای وظیفه بگیر
     * مورد mos برای مستمری بگیر سازمانی
     * مورد su برای کاربر سوپر یوزر
     * مورد guest برای کاربر مهمان
     * emlpoyee کارکنان صندوق
     */
    @SerializedName("personType")
    private String personType;
    @SerializedName("activeCode")
    private String activeCode;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("acceptCode")
    private String acceptCode;
    @SerializedName("pass")
    private String password;


    //i dont khnow what is grant type but DefaultValue is 'password'
    @SerializedName("grantType")
    private String grantType = "password";

    //Token
    @SerializedName("access_token")
    private String token;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private String tokenExpireDate;
    @SerializedName("fullName")
    private String fullName;
    //قسمت نا معتبر کد
    //به دلیل کیفیت پایین دیتای ارسالی از سمت سرور مجبور به این کار شدم!!
    @SerializedName("userData")
    private ArrayList<User> userExtrasInfo;

    private String loginDate;



    public long getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(long personalCode) {
        this.personalCode = personalCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileDeviceBrand() {
        return mobileDeviceBrand;
    }

    public void setMobileDeviceBrand(String mobileDeviceBrand) {
        this.mobileDeviceBrand = mobileDeviceBrand;
    }

    public String getMobileImei() {
        return mobileImei;
    }

    public void setMobileImei(String mobileImei) {
        this.mobileImei = mobileImei;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }


    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAcceptCode() {
        return acceptCode;
    }

    public void setAcceptCode(String acceptCode) {
        this.acceptCode = acceptCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenExpireDate() {
        return tokenExpireDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setTokenExpireDate(String tokenExpireDate) {
        this.tokenExpireDate = tokenExpireDate;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public ArrayList<User> getUserExtrasInfo() {
        return userExtrasInfo;
    }

    public void setUserExtrasInfo(ArrayList<User> userExtrasInfo) {
        this.userExtrasInfo = userExtrasInfo;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getPersonTypeName() {
        if (TextUtils.isEmpty(personType)) {
            return "کاربر نامشخص";
        }

        if (personType.equals("baz"))
            return "کاربر بازنشسته";

        if (personType.equals("Vaz"))
            return "کاربر وظیفه بگیر";

        if (personType.equals("mos"))
            return "کاربر مستمری بگیر سازمانی";

        if (personType.equals("su"))
            return "کاربر سوپر یوزر";

        if (personType.equals("guest"))
            return "کاربر  مهمان";

        if (personType.equals("employee"))
            return "کاربر صندوق";

        return "کاربر نامشخص";
    }

    public boolean isPersonGuest() {

        if (!TextUtils.isEmpty(personType))
            if (personType.equals("guest"))
                return true;

        return false;
    }

    public boolean isPersonSuperUser() {

        if (!TextUtils.isEmpty(personType))
            if (personType.equals("su"))
                return true;

        return false;
    }


    public User() {
    }

    public User(Cursor in) {

        if (in == null)
            return;

        try {
            if (in.getColumnIndex(UserData.USR_FIRST_NAME) != -1) {
                this.firstName = in.getString(in.getColumnIndex(UserData.USR_FIRST_NAME));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (in.getColumnIndex(UserData.USR_PERSONAL_CODE) != -1) {
                this.personalCode = in.getLong(in.getColumnIndex(UserData.USR_PERSONAL_CODE));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (in.getColumnIndex(UserData.USR_LAST_NAME) != -1) {
                this.lastName = in.getString(in.getColumnIndex(UserData.USR_LAST_NAME));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (in.getColumnIndex(UserData.USR_PERSON_TYPE) != -1) {
                this.personType = in.getString(in.getColumnIndex(UserData.USR_PERSON_TYPE));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (in.getColumnIndex(UserData.USR_TOKEN) != -1) {
                this.token = in.getString(in.getColumnIndex(UserData.USR_TOKEN));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (in.getColumnIndex(UserData.USR_TOKEN_TYPE) != -1) {
                this.tokenType = in.getString(in.getColumnIndex(UserData.USR_TOKEN_TYPE));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (in.getColumnIndex(UserData.USR_MOBILE) != -1) {
                this.mobile = in.getString(in.getColumnIndex(UserData.USR_MOBILE));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (in.getColumnIndex(UserData.USR_LOGIN_DATE) != -1) {
                this.loginDate = in.getString(in.getColumnIndex(UserData.USR_LOGIN_DATE));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean isUserDataComplete(){
        if (personalCode==0)
            return false;

        if (TextUtils.isEmpty(firstName)||TextUtils.isEmpty(lastName))
            return false;

        if (TextUtils.isEmpty(token)||TextUtils.isEmpty(personType)||TextUtils.isEmpty(tokenType))
            return false;

        return true;

    }
}
