package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.INews;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.INewsApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsServerRepo implements INews {

    Call<NewsWrapper> call;

    /**
     * @param context
     * @param name      name of news . in default name = news
     * @param page      number of page
     * @param tokenType
     * @param token
     * @param callBack
     */
    @Override
    public void news(Context context, String name, long page, String tokenType, String token, IRepoCallBack<NewsWrapper> callBack) {

        INewsApi newsApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(INewsApi.class);
        call = newsApi.news(name, page);
        call.enqueue(new Callback<NewsWrapper>() {
            @Override
            public void onResponse(Call<NewsWrapper> call, Response<NewsWrapper> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اخبار جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getNews() == null || response.body().getNews().size() == 0) {
                    callBack.onError(new Throwable("اخبار جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());

            }

            @Override
            public void onFailure(Call<NewsWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("اخبار جهت نمایش وجود ندارد"));
            }
        });


    }
}
