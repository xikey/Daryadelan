package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

public class AppInfo {

    @SerializedName("appVersionId")
    private long appVersionID;
    @SerializedName("appVersionName")
    private String appVersionName;
    @SerializedName("appVersionDescription")
    private  String appVersinDescription;
    @SerializedName("appPath")
    private String appPath;
    @SerializedName("appVersionNumber")
    private String appVersionNumber;
    @SerializedName("isStableVersion")
    private boolean isStableVersion;
    @SerializedName("createdat")
    private String createDate;

}
