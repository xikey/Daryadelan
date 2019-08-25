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


    public long getAppVersionID() {
        return appVersionID;
    }

    public void setAppVersionID(long appVersionID) {
        this.appVersionID = appVersionID;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getAppVersinDescription() {
        return appVersinDescription;
    }

    public void setAppVersinDescription(String appVersinDescription) {
        this.appVersinDescription = appVersinDescription;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getAppVersionNumber() {
        return appVersionNumber;
    }

    public void setAppVersionNumber(String appVersionNumber) {
        this.appVersionNumber = appVersionNumber;
    }

    public boolean isStableVersion() {
        return isStableVersion;
    }

    public void setStableVersion(boolean stableVersion) {
        isStableVersion = stableVersion;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
