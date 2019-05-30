package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface IUser {

    void personCheck(Context context, User user, IRepoCallBack<User> callBack);

    void checkSMSisValidate(Context context, User user, IRepoCallBack<User> callBack);

    void createUser(Context context, User user, IRepoCallBack<User> callBack);

    void login(Context context, User user, IRepoCallBack<User> callBack);


}
