package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.model.Camp;

public class CampsWrapper extends ServerWrapper {

    @SerializedName("data")
    private ArrayList<Camp> camps;

    public ArrayList<Camp> getCamps() {
        return camps;
    }

    public void setCamps(ArrayList<Camp> camps) {
        this.camps = camps;
    }
}
