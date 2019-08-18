package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;

import daryadelan.sandogh.zikey.com.daryadelan.model.CheckActivationCode;
import daryadelan.sandogh.zikey.com.daryadelan.model.LoginDto;
import daryadelan.sandogh.zikey.com.daryadelan.model.PersonCheck;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.UserCreation;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IUserApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServerRepo implements IUser {

    Call<User> userCall;
    //مقدار پیش فرض توکن دریافتی. این مقدار از سمت سرور قرار داده شده ...
    private final String KEY_TOKEN_TYPE = "bearer";


    @Override
    public void personCheck(Context context, User user, final IRepoCallBack<User> callBack) {

        IUserApi userApi = ServerApiClient.getClient(context).create(IUserApi.class);

        PersonCheck o = new PersonCheck();
        o.mobile = user.getMobile();
        o.mobileDeviceBrand = user.getMobileDeviceBrand();
        o.mobileImei = user.getMobileImei();
        o.osVersion = user.getOsVersion();
        o.personalCode = user.getPersonalCode();

        userCall = userApi.checkPerson(o);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.raw() != null && response.raw().code() == 404) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }

                if (  response.raw().code() == 500) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }
                if (response.errorBody() != null) {

                    Gson gson = new Gson();
                    try {
                        User user = gson.fromJson(response.errorBody().string(), User.class);
                        callBack.onError(new Throwable(user.getError_description()));
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
        CheckActivationCode o = new CheckActivationCode();
        o.activeCode = user.getActiveCode();
        o.mobile = user.getMobile();
        userCall = userApi.checkSMSisValidate(o);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.raw() != null && response.raw().code() == 404) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }
                if (  response.raw().code() == 500) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }

                if (response.errorBody() != null) {

                    Gson gson = new Gson();
                    try {
                        User user = gson.fromJson(response.errorBody().string(), User.class);
                        callBack.onError(new Throwable(user.getError_description()));
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
        UserCreation o = new UserCreation();
        o.acceptCode = user.getAcceptCode();
        o.firstName = user.getFirstName();
        o.lastName = user.getLastName();
        o.mobile = user.getMobile();
        o.password = user.getPassword();
        o.personType = user.getPersonType();
        userCall = userApi.createUser(o);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.raw() != null && response.raw().code() == 404) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }
                if (  response.raw().code() == 500) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }

                if (response.errorBody() != null) {

                    Gson gson = new Gson();
                    try {
                        User user = gson.fromJson(response.errorBody().string(), User.class);
                        callBack.onError(new Throwable(user.getError_description()));
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
//        userCall = userApi.login(user.getMobile(), user.getPassword(), user.getGrantType());
        LoginDto obj = new LoginDto(user.getMobile(), user.getPassword(), "pass");
        userCall = userApi.login(obj);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.raw() != null && response.raw().code() == 404) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }
                if (  response.raw().code() == 500) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }

                if (response.errorBody() != null) {

                    Gson gson = new Gson();
                    try {
                        User user = gson.fromJson(response.errorBody().string(), User.class);
                        callBack.onError(new Throwable(user.getError_description()));
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("خطا در ورود به برنامه"));
                    return;
                }


                if (response.body().getResultId() < 0) {
                    if (!TextUtils.isEmpty(response.body().getMessagee())) {
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
    public void exitApp(final Context context, final IRepoCallBack<User> callBack) {

    }

    @Override
    public void userInfo(Context context, String tokenType, String token, final IRepoCallBack<User> callBack) {

        IUserApi userApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(IUserApi.class);
        userCall = userApi.userInfo();
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.raw() != null && response.raw().code() == 404) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }

                if (  response.raw().code() == 500) {
                    callBack.onError(new Throwable("متاسفانه خطایی در دریافت اطلاعات رخ داده است. لطفا مجددا تلاش نمایید"));
                    return;
                }

                if (response.errorBody() != null) {

                    Gson gson = new Gson();
                    try {
                        User user = gson.fromJson(response.errorBody().string(), User.class);
                        callBack.onError(new Throwable(user.getError_description()));
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if (response.body() == null) {
                    callBack.onError(new Throwable("خطا در ورود به برنامه"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    if (!TextUtils.isEmpty(response.body().getMessagee())) {
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

    @Override
    public void saveUserDatas(Context context, User user, IRepoCallBack<User> callBack) {

    }

    @Override
    public void updateUserDatas(Context context, User user, IRepoCallBack<User> callBack) {

    }

    @Override
    public void getUserDatas(Context context, IRepoCallBack<User> callBack) {

    }


}
