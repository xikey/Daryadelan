package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;


import androidx.annotation.Nullable;

import daryadelan.sandogh.zikey.com.daryadelan.model.CheckActivationCode;
import daryadelan.sandogh.zikey.com.daryadelan.model.LoginDto;
import daryadelan.sandogh.zikey.com.daryadelan.model.PersonCheck;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.UserCreation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Zikey on 19/09/2017.
 */

public interface IUserApi {


    @POST("api/persons/personcheck")
    Call<User> checkPerson(@Body PersonCheck personCheck);



    @POST("api/persons/CheckActivationCode")
    Call<User> checkSMSisValidate(@Body CheckActivationCode checkActivationCode);


    @POST("api/persons/usercreation")
    Call<User> createUser(@Body UserCreation obj);


    @POST("api/auth/login")
    Call<User> login(@Body LoginDto loginDto);

    @GET("api/auth/GetUserInfo")
    Call<User> userInfo();


}
