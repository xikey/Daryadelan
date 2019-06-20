package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AdvertiseWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IAdvertise;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IAdvertiseApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertiseServerRepo implements IAdvertise {

    private static final String KEY_MAIN_SLIDER = "MainSlider";

    Call<AdvertiseWrapper> call;
    @Override
    public void getAdvertise(Context context, String name, final IRepoCallBack<AdvertiseWrapper> callBack) {

        IAdvertiseApi advertiseApi = ServerApiClient.getClientWithHeader(context).create(IAdvertiseApi.class);
        call = advertiseApi.getMainSlider(KEY_MAIN_SLIDER);
        call.enqueue(new Callback<AdvertiseWrapper>() {
            @Override
            public void onResponse(Call<AdvertiseWrapper> call, Response<AdvertiseWrapper> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اطلاعاتی جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getData() == null || response.body().getData().size() == 0) {
                    callBack.onError(new Throwable("اطلاعاتی جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<AdvertiseWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("خطا در دریافت اطلاعات. لطفا مجددا تلاش نمایید"));
            }
        });

    }
}
