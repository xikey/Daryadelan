package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface INews {

    /**
     *
     * @param context
     * @param name name of news . in default name = news
     * @param page
     * @param tokenType
     * @param token
     * @param callBack
     */
    void news(Context context, String name, long page, String tokenType, String token, IRepoCallBack<NewsWrapper> callBack);
}
