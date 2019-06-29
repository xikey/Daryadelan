package daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AhkamWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.HokmWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.PayrollWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;

public interface IPayroll {

    void allAvailablePayrolls(Context context,long personelCode, IRepoCallBack<PayrollWrapper> callBack);

    void allAvailableAhkam(Context context,long personelCode, IRepoCallBack<AhkamWrapper> callBack);

    void getPayroll(Context context, long year, long month,long personelCode, IRepoCallBack<PayrollWrapper> callBack);

    void getHokm(Context context, String year, long personelCode, IRepoCallBack<HokmWrapper> callBack);


}
