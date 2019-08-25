package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AppInfoServerWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IAppInfo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IAdvertiseApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IAppinfoApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.ICampApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppInfoServerRepo implements IAppInfo {

    Call<AppInfoServerWrapper> call;

    @Override
    public void appVersionChecker(Context context, String appVersion, String tokenType, String token, IRepoCallBack<AppInfoServerWrapper> callBack) {

        IAppinfoApi appinfoApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(IAppinfoApi.class);
        call = appinfoApi.getLatestVersion(appVersion);
        call.enqueue(new Callback<AppInfoServerWrapper>() {
            @Override
            public void onResponse(Call<AppInfoServerWrapper> call, Response<AppInfoServerWrapper> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اطلاعات جهت دریافت وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getAppInfo() == null) {
                    callBack.onError(new Throwable("اطلاعات جهت دریافت وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<AppInfoServerWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("اطلاعات جهت دریافت وجود ندارد"));
                return;
            }
        });


    }
}
