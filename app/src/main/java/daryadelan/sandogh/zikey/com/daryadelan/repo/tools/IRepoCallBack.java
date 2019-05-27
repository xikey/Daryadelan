package daryadelan.sandogh.zikey.com.daryadelan.repo.tools;


import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;

/**
 * Created by Zikey on 18/07/2017.
 */

public interface IRepoCallBack<T extends ServerWrapper> {
    public void onAnswer(T answer);
    public void onError(Throwable error);
    public void onCancel();
    public void onProgress(int p);
}
