package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;


import android.support.annotation.Nullable;

import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Zikey on 19/09/2017.
 */

public interface IUserApi {

    @FormUrlEncoded
    @POST("api/persons/personcheck")
    Call<User> checkPerson(@Nullable @Field("PersonalCode") String PersonalCode,
                           @Field("Mobile") String Mobile,
                           @Field("MobileDeviceBrand") String MobileDeviceBrand,
                           @Field("MobileImei") String MobileImei,
                           @Field("OsVersion") String OsVersion,
                           @Field("PersonType") String PersonType);


    @FormUrlEncoded
    @POST("api/persons/CheckActivationCode")
    Call<User> checkSMSisValidate(@Nullable @Field("ActiveCode") String ActiveCode,
                                  @Field("Mobile") String Mobile);

    @FormUrlEncoded
    @POST("api/persons/usercreation")
    Call<User> createUser( @Field("AcceptCode") String AcceptCode,
                           @Field("Mobile") String Mobile,
                           @Field("Password") String Password,
                           @Field("FirstName") String FirstName,
                           @Field("LastName") String LastName);



}
