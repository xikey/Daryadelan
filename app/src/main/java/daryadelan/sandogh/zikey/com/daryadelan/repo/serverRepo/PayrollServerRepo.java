package daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PayrollWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient.ServerApiClient;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IPayroll;
import daryadelan.sandogh.zikey.com.daryadelan.repo.retrofitCalls.IPayrollApi;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayrollServerRepo implements IPayroll {

    Call<PayrollWrapper> call;

    @Override
    public void allAvailablePayrolls(Context context, final IRepoCallBack<PayrollWrapper> callBack) {

        IPayrollApi payrollApi = ServerApiClient.getClientWithHeader(context).create(IPayrollApi.class);
        call = payrollApi.allAvailablePayrolls();
        call.enqueue(new Callback<PayrollWrapper>() {
            @Override
            public void onResponse(Call<PayrollWrapper> call, Response<PayrollWrapper> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اطلاعاتی جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getData() == null || response.body().getData().size() == 0) {
                    callBack.onError(new Throwable("اطلاعاتی جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());

            }

            @Override
            public void onFailure(Call<PayrollWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("خطا در دریافت اطلاعات. لطفا مجددا تلاش نمایید"));
            }
        });
    }

    @Override
    public void getPayroll(Context context, long year, long month, final IRepoCallBack<PayrollWrapper> callBack) {

        IPayrollApi payrollApi = ServerApiClient.getClientWithHeader(context).create(IPayrollApi.class);
        call = payrollApi.getPayroll(year, month);
        call.enqueue(new Callback<PayrollWrapper>() {
            @Override
            public void onResponse(Call<PayrollWrapper> call, Response<PayrollWrapper> response) {

                if (response == null) {
                    callBack.onError(new Throwable("RP ERR 101  خطا در دریافت اطلاعات"));
                    return;
                }

                if (response.body() == null) {
                    callBack.onError(new Throwable("اطلاعاتی جهت نمایش وجود ندارد"));
                    return;
                }

                if (response.body().getResultId() < 0) {
                    callBack.onError(new Throwable(response.body().getMessagee()));
                    return;
                }

                if (response.body().getData() == null || response.body().getData().size() == 0) {
                    callBack.onError(new Throwable("اطلاعاتی جهت نمایش وجود ندارد"));
                    return;
                }

                callBack.onAnswer(response.body());

            }


            @Override
            public void onFailure(Call<PayrollWrapper> call, Throwable throwable) {
                callBack.onError(new Throwable("خطا در دریافت اطلاعات. لطفا مجددا تلاش نمایید"));
            }
        });


    }
}
