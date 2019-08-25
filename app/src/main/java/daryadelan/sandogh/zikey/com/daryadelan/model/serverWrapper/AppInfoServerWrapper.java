package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

import daryadelan.sandogh.zikey.com.daryadelan.model.AppInfo;

public class AppInfoServerWrapper extends ServerWrapper{


    @SerializedName("data")
    private AppInfo appInfo;

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }
}
