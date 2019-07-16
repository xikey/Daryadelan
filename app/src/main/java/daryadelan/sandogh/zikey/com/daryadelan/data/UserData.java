package daryadelan.sandogh.zikey.com.daryadelan.data;

import com.google.gson.annotations.SerializedName;

public class UserData {


    // DB TABLE NAME
    public static final String TABLE_USER= "USER";

    // DB column name
    public static final String USR_ROW = "USR_ROW";
    public static final String USR_PERSONAL_CODE = "USR_PERSONAL_CODE";
    public static final String USR_FIRST_NAME = "USR_FIRST_NAME";
    public static final String USR_LAST_NAME = "USR_LAST_NAME";
    public static final String USR_PERSON_TYPE = "USR_PERSON_TYPE";
    public static final String USR_TOKEN = "USR_TOKEN";
    public static final String USR_TOKEN_TYPE = "USR_TOKEN_TYPE";
    public static final String USR_TOKEN_EXPIRE = "USR_TOKEN_EXPIRE";
    public static final String USR_MOBILE = "USR_MOBILE";
    public static final String USR_LOGIN_DATE = "USR_LOGIN_DATE";

    // DB create table
    public static final String  CREATE_TABLE = "create table " + TABLE_USER
            + "(" + USR_ROW + " INTEGER PRIMARY KEY  AUTOINCREMENT,"
            + USR_PERSONAL_CODE + " INTEGER,"
            + USR_LAST_NAME + " TEXT,"
            + USR_PERSON_TYPE + " TEXT,"
            + USR_TOKEN + " TEXT,"
            + USR_TOKEN_TYPE + " TEXT,"
            + USR_TOKEN_EXPIRE + " TEXT,"
            + USR_MOBILE + " TEXT,"
            + USR_LOGIN_DATE + " TEXT,"
            + USR_FIRST_NAME + " TEXT" + ");";


    private long personalCode;
    private String firstName;
    private String lastName;
    private String personType;
    private String token;
    private String tokenType;
    private String tokenExpireDate;
    private String mobile;
    private String loginDate;
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
    private String userType;

}
