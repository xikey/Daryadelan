package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.model.Payroll;

public class HokmWrapper extends  ServerWrapper {

    @SerializedName("filesData")
    private ArrayList<Payroll> data;

    public ArrayList<Payroll> getData() {
        return data;
    }

    public void setData(ArrayList<Payroll> data) {
        this.data = data;
    }
}
