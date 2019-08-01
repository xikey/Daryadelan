package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;


import android.support.annotation.Nullable;

import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Zikey on 19/09/2017.
 */

public interface IUserApi {


    @POST("api/persons/personcheck")
    Call<User> checkPerson(@Body User user);



    @POST("api/persons/CheckActivationCode")
    Call<User> checkSMSisValidate(@Body User user);


    @POST("api/persons/usercreation")
    Call<User> createUser(@Body User user);


    @POST("api/auth/login")
    Call<User> login(@Body User user);

    @GET("api/accounts/GetUserInfo")
    Call<User> userInfo();


}
