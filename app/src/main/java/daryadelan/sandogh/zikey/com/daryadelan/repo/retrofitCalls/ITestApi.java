package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Zikey on 19/09/2017.
 */

public interface ITestApi {


    @GET("Discounts/getDiscountsTest")
    Call<ResponseBody> test();






}
