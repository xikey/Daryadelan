package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;
import daryadelan.sandogh.zikey.com.daryadelan.model.CampRequest;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampReseptionRequesWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsHistoryWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICampApi {

    @GET("api/Camps")
    Call<CampsWrapper> allCamps();

    @POST("api/CampRequests/insertCampRequest")
    Call<CampReseptionRequesWrapper> reauestCamp(@Body CampRequest camp);

    @GET("api/CampRequests/GetCampRequests")
    Call<CampsHistoryWrapper> getAllCampRequests(@Query("page") int page);



}
