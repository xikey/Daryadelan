package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.model.CampRequest;

public class CampsHistoryWrapper extends ServerWrapper {

    @SerializedName("data")
    private ArrayList<CampRequest> campRequests;

    public ArrayList<CampRequest> getCampRequests() {
        return campRequests;
    }

    public void setCampRequests(ArrayList<CampRequest> campRequests) {
        this.campRequests = campRequests;
    }
}
