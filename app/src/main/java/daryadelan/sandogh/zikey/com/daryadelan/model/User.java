package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;

public class User extends ServerWrapper {

    @SerializedName("personalCode")
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
     * یکی از موارد زیر را می پذیرد:
     * Baz
     * Vaz
     * Mos
     * مورد baz برای بازنشسته
     * مورد vaz برای وظیفه بگیر
     * مورد mos برای مستمری بگیر سازمانی
     */
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
}
