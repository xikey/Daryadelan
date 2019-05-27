package daryadelan.sandogh.zikey.com.daryadelan.repo.tools;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;


/**
 * Created by Zikey on 18/07/2017.
 */

public interface IRepo<T extends ServerWrapper> {
   void cancel(Context c);
   void get(Context context, Object id, IRepoCallBack<T> callBack);
   void insert(Context context, T newObject, IRepoCallBack<T> callBack);
   void update(Context context, Object id, T updateObject, IRepoCallBack callBack);
   void delete(Context context, Object id, IRepoCallBack<T> callBack);


}
