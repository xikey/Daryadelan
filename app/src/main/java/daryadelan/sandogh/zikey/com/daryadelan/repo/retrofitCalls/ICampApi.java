package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICampApi {

    @GET("api/Camps")
    Call<CampsWrapper> allCamps();

    @POST("api/CampRequests/insertCampRequest")
    Call<CampsWrapper> reauestCamp(Camp camp);

    @GET("api/CampRequests/GetCampRequests")
    Call<CampsWrapper> getAllCampRequests(@Query("page") int page);



}
