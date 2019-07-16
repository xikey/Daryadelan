package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.Photo;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PhotosWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPhotos;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.INewsApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IPhotosApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosServerRepo implements IPhotos {

    Call<PhotosWrapper> call;

    @Override
    public void galleries(Context context, long page, String tokenType, String token, IRepoCallBack<PhotosWrapper> callBack) {

        IPhotosApi photosApi = ServerApiClient.getClientWithHeader(context, tokenType, token).create(IPhotosApi.class);
        call = photosApi.galleries(page);
        call.enqueue(new Callback<PhotosWrapper>() {
            @Override
            public void onResponse(Call<PhotosWrapper> call, Response<PhotosWrapper> response) {
                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("گالری جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getPhotos() == null || response.body().getPhotos().size() == 0) {
                    callBack.onError(new Throwable("گالری جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());

            }

            @Override
            public void onFailure(Call<PhotosWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("گالری جهت نمایش وجود ندارد"));
            }
        });

    }
}
