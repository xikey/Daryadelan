package daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls;

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
    Call<PayrollWrapper> allAvailablePayrolls();

    @GET("api/Payrolls/GetPayrollItem")
    Call<PayrollWrapper> getPayroll(@Query("year") long year
            , @Query("month") long month);

}
