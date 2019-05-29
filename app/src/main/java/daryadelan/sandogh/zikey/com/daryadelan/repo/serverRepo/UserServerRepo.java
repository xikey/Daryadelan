package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IUserApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServerRepo implements IUser {

    Call<User> userCall;


    @Override
    public void personCheck(Context context, User user, final IRepoCallBack<User> callBack) {

        IUserApi userApi = ServerApiClient.getClient(context).create(IUserApi.class);
        userCall = userApi.checkPerson(String.valueOf(user.getPersonalCode()), user.getMobile(), user.getMobileDeviceBrand(), user.getMobileImei(), user.getOsVersion(), user.getPersonType());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("RP ERR 102  response body is null"));
                    return;
                }


                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                callBack.onError(new Throwable("خطا در دریافت اطلاعات"));
            }
        });
    }

    @Override
    public void checkSMSisValidate(Context context, User user, final IRepoCallBack<User> callBack) {
        IUserApi userApi = ServerApiClient.getClient(context).create(IUserApi.class);
        userCall = userApi.checkSMSisValidate(user.getActiveCode(),user.getMobile());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("RP ERR 102  response body is null"));
                    return;
                }


                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }


                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                callBack.onError(new Throwable("خطا در دریافت اطلاعات"));
            }
        });
    }

    @Override
    public void createUser(Context context, User user, final IRepoCallBack<User> callBack) {

        IUserApi userApi = ServerApiClient.getClient(context).create(IUserApi.class);
        userCall = userApi.createUser(user.getAcceptCode(),user.getMobile(),user.getPassword(),user.getFirstName(),user.getLastName());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("RP ERR 102  response body is null"));
                    return;
                }


                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }


                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                callBack.onError(new Throwable("خطا در دریافت اطلاعات"));
            }
        });

    }


}
