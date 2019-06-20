package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.model.Advertise;

public class AdvertiseWrapper extends ServerWrapper {

    @SerializedName("data")
    private ArrayList<Advertise> data;

    public ArrayList<Advertise> getData() {
        return data;
    }

    public void setData(ArrayList<Advertise> data) {
        this.data = data;
    }
}
