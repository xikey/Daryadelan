package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AdvertiseWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IAdvertiseApi {

    @GET("api/Gallery/GetGalleryByName")
    Call<AdvertiseWrapper> getMainSlider(@Query("name") String name);
}
