package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;


import android.support.annotation.Nullable;

import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Zikey on 19/09/2017.
 */

public interface IUserApi {

    @FormUrlEncoded
    @POST("api/persons/personcheck")
    Call<User> checkPerson(
            @Field("PersonalCode") long nationalCode,
            @Field("Mobile") String Mobile,
            @Field("MobileDeviceBrand") String MobileDeviceBrand,
            @Field("MobileImei") String MobileImei,
            @Field("OsVersion") String OsVersion);


    @FormUrlEncoded
    @POST("api/persons/CheckActivationCode")
    Call<User> checkSMSisValidate(@Nullable @Field("ActiveCode") String ActiveCode,
                                  @Field("Mobile") String Mobile);

    @FormUrlEncoded
    @POST("api/persons/usercreation")
    Call<User> createUser(@Field("AcceptCode") String AcceptCode,
                          @Field("Mobile") String Mobile,
                          @Field("Password") String Password,
                          @Field("FirstName") String FirstName,
                          @Field("LastName") String LastName,
                          @Field("PersonType") String personType);

    /**
     * @param mobile     user name is mobile number
     * @param password   password
     * @param Grant_type i dont know what is this!! but default value must be 'password'
     * @return
     */
    @FormUrlEncoded
    @POST("oauth/token")
    Call<User> login(@Field("username") String mobile,
                     @Field("password") String password,
                     @Field("grant_type") String Grant_type);


    @GET("api/accounts/GetUserInfo")
    Call<User> userInfo();


}
