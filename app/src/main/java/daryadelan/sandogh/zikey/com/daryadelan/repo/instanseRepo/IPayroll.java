package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PayrollWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface IPayroll {

    void allAvailablePayrolls(Context context, IRepoCallBack<PayrollWrapper> callBack);

    void getPayroll(Context context, long year, long month, IRepoCallBack<PayrollWrapper> callBack);

}
