package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INewsApi {

    @GET("api/News/GetNews")
    Call<NewsWrapper> news(@Query("name") String name,
                           @Query("page") Long page);
}
