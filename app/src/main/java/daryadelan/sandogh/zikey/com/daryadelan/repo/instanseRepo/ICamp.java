package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampReseptionRequesWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsHistoryWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface ICamp {

    void allCamps(Context context, String tokenType, String token, IRepoCallBack<CampsWrapper> callBack);

    void requestCamp(Context context, Camp camp, String tokenType, String token, User user, IRepoCallBack<CampReseptionRequesWrapper> callBack);

    void campRequestsHistory(Context context, int page, String tokenType, String token, IRepoCallBack<CampsHistoryWrapper> callBack);

    void campRequestHistoryByID(Context context,long requestID, String tokenType, String token, IRepoCallBack<CampsHistoryWrapper> callBack);
}
