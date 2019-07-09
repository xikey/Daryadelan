package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AdvertiseWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface IAdvertise {
    /**
     *
     * @param context
     * @param name name of the type of Advertises like MainSlider
     * @param callBack
     */
    void getAdvertise(Context context, String name,String tokenType,String token, IRepoCallBack<AdvertiseWrapper> callBack);
}
