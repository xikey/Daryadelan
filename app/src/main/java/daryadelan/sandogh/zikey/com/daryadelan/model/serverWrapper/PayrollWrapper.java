package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.model.Payroll;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;

public class PayrollWrapper extends ServerWrapper {

    @SerializedName("data")
    private ArrayList<Payroll> data;
    @SerializedName("data2")
    private User user;

    public ArrayList<Payroll> getData() {
        return data;
    }

    public void setData(ArrayList<Payroll> data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
