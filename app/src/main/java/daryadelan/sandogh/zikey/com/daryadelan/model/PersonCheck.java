package daryadelan.sandogh.zikey.com.daryadelan.model;

public class PersonCheck {
    public long personalCode;
    public String mobile;
    public String mobileDeviceBrand;
    public String mobileImei;
    public String osVersion;

    public PersonCheck() {
    }


    public PersonCheck(long PersonalCode, String Mobile, String MobileDeviceBrand, String MobileImei, String OsVersion) {

        personalCode = PersonalCode;
        mobile = Mobile;
        mobileDeviceBrand = MobileDeviceBrand;
        mobileImei = MobileImei;
        osVersion = OsVersion;
    }
}
