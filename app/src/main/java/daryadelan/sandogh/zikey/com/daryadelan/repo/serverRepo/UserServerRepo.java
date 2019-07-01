package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;
import android.text.TextUtils;

import com.razanpardazesh.razanlibs.Tools.AsyncWrapper;

import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IUserApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.SqlAsyncWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServerRepo implements IUser {

    Call<User> userCall;
    //مقدار پیش فرض توکن دریافتی. این مقدار از سمت سرور قرار داده شده ...
    private   final String KEY_TOKEN_TYPE = "bearer";


    @Override
    public void personCheck(Context context, User user, final IRepoCallBack<User> callBack) {

        IUserApi userApi = ServerApiClient.getClient(context).create(IUserApi.class);
        userCall = userApi.checkPerson( user.getMobile(), user.getMobileDeviceBrand(), user.getMobileImei(), user.getOsVersion(), user.getPersonType());
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
        userCall = userApi.checkSMSisValidate(user.getActiveCode(), user.getMobile());
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
        userCall = userApi.createUser(user.getAcceptCode(), user.getMobile(), user.getPassword(), user.getFirstName(), user.getLastName(),user.getPersonType());
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
    public void login(Context context, User user, final IRepoCallBack<User> callBack) {
        IUserApi userApi = ServerApiClient.getClient(context).create(IUserApi.class);
        userCall = userApi.login(user.getMobile(), user.getPassword(), user.getGrantType());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("خطا در ورود به برنامه"));
                    return;
                }

                if (response.body().getResultId()<0){
                    if (!TextUtils.isEmpty(response.body().getMessagee())){
                        callBack.onError(new Throwable(response.body().getMessagee()));
                        return;
                    }

                }


                if (TextUtils.isEmpty(response.body().getToken())) {
                    callBack.onError(new Throwable("خطا در دریافت اطلاعات token"));
                    return;
                }
                 if (!response.body().getTokenType().equals(KEY_TOKEN_TYPE)) {
                    callBack.onError(new Throwable("نا معتبر token"));
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
    public void exitApp(Context context, final IRepoCallBack<User> callBack) {
        final SqlAsyncWrapper asyncWrapper = new SqlAsyncWrapper();

        asyncWrapper.setDoOnBackground(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                User answer = new User() {
                };
                try {

                    SessionManagement.getInstance(asyncWrapper.getContext()).clearMemberData();

                    answer.setResultId(0);
                } catch (Exception e) {
                    answer.setResultId(-1);
                    e.printStackTrace();
                }

                return answer;
            }
        }).setDoOnAnswer(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                if (o == null)
                    callBack.onAnswer(null);
                else if (o instanceof User)
                    callBack.onAnswer((User) o);
                return null;
            }
        }).setDoOnError(new AsyncWrapper.Callback() {
            @Override
            public Object call(Object o) {
                if (o instanceof Throwable)
                    callBack.onError((Throwable) o);

                return null;
            }
        }).run(context);

    }

    @Override
    public void userInfo(Context context, final IRepoCallBack<User> callBack) {

        IUserApi userApi = ServerApiClient.getClientWithHeader(context).create(IUserApi.class);
        userCall = userApi.userInfo( );
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("خطا در ورود به برنامه"));
                    return;
                }

                if (response.body().getResultId()<0){
                    if (!TextUtils.isEmpty(response.body().getMessagee())){
                        callBack.onError(new Throwable(response.body().getMessagee()));
                        return;
                    }

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
