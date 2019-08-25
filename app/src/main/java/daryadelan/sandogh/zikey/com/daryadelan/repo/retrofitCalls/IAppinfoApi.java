package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AppInfoServerWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.CampsWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IAppinfoApi {

    @GET("api/version/latestversion")
    Call<AppInfoServerWrapper> getLatestVersion(@Query("version") String version);

}
