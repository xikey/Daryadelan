package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.ICamp;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.ICampApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.INewsApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampServerRepo implements ICamp {


    Call<CampsWrapper> call;

    @Override
    public void allCamps(Context context, String tokenType, String token, IRepoCallBack<CampsWrapper> callBack) {

        ICampApi campApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(ICampApi.class);
        call = campApi.allCamps();
        call.enqueue(new Callback<CampsWrapper>() {
            @Override
            public void onResponse(Call<CampsWrapper> call, Response<CampsWrapper> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اطلاعات جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getCamps() == null || response.body().getCamps().size() == 0) {
                    callBack.onError(new Throwable("اخبار جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<CampsWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("اخبار جهت نمایش وجود ندارد"));
                return;
            }
        });
    }

    @Override
    public void requestCamp(Context context, Camp camp, String tokenType, String token, IRepoCallBack<CampsWrapper> callBack) {

        ICampApi campApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(ICampApi.class);
        call = campApi.reauestCamp(camp);
        call.enqueue(new Callback<CampsWrapper>() {
            @Override
            public void onResponse(Call<CampsWrapper> call, Response<CampsWrapper> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در ثبت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("RP ERR 102  خطا در ثبت اطلاعات"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                callBack.onAnswer(response.body());


            }

            @Override
            public void onFailure(Call<CampsWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("RP ERR 103  خطا در ثبت اطلاعات"));
            }
        });

    }

    @Override
    public void campRequestsHistory(Context context, int page, String tokenType, String token, IRepoCallBack<CampsWrapper> callBack) {

    }


}
