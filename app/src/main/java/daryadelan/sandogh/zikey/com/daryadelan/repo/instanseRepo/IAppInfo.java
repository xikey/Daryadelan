package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AppInfoServerWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface IAppInfo {

    void appVersionChecker(Context context,String appVersion, String tokenType, String token, IRepoCallBack<AppInfoServerWrapper> callBack);


}
