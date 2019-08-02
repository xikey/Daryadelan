package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PhotosWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPhotosApi {

    @GET("api/Galleries/GetGalleries")
    Call<PhotosWrapper> galleries(@Query("page") Long page);
}
