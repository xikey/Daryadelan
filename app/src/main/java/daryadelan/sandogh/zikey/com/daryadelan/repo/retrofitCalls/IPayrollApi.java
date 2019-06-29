package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AhkamWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.HokmWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PayrollWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPayrollApi {

//    @GET("Payrolls/GetPayrollYm")
//    Call<PayrollWrapper> allAvailablePayrolls(@Query("fromDate") String fromDate,
//                                         @Query("toDate") String toDate,
//                                         @Query("type") int type);


    @GET("api/Payrolls/GetPayrollYm")
    Call<PayrollWrapper> allAvailablePayrolls(@Query("pc") long personalCode);

    @GET("api/Payrolls/GetPayrollItem")
    Call<PayrollWrapper> getPayroll(@Query("year") long year
            , @Query("month") long month
            , @Query("pc") long PersonelCode);


    @GET("api/hokm/getallhokmname")
    Call<AhkamWrapper> allAvailableAhkam(@Query("pc") long personalCode );

    @GET("api/hokm/gethokm")
    Call<HokmWrapper> getHokm(@Query("year") String year
            , @Query("pc") long personalCode);
}
