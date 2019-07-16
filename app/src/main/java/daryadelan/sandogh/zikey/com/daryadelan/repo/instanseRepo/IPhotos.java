package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PhotosWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface IPhotos {

    /**
     *
     * @param context
     * @param page
     * @param tokenType
     * @param token
     * @param callBack
     */
    void galleries(Context context,  long page, String tokenType, String token, IRepoCallBack<PhotosWrapper> callBack);
}
