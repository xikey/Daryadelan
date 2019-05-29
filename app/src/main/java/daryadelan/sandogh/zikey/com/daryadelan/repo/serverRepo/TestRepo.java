package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.ITestApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestRepo {
    Call<ResponseBody> responseBodyCall;


    public void testing(Context context) {

        ITestApi testApi = ServerApiClient.getClient(context).create(ITestApi.class);
        responseBodyCall = testApi.test();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response == null) {

                    return;
                }

                if (response.body() == null) {

                    return;
                }


//                if (response.body().getResultId() < 0) {
//                    callBack.onError(new Throwable("RP ERR 104  IsSuccess"));
//                    return;
//                }
//
//                if (!TextUtils.isEmpty(response.body().getMessagee())) {
//                    callBack.onError(new Throwable( response.body().getMessagee()));
//                    return;
//                }


//                callBack.onAnswer(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                if (throwable != null) {

                }
            }
        });
    }

}
