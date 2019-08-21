package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.model.CampRequest;
import daryadelan.sandogh.zikey.com.daryadelan.model.CampRequestHistory;

public class CampsHistoryWrapper extends ServerWrapper {

    @SerializedName("data")
    private ArrayList<CampRequestHistory> campRequests;

    public ArrayList<CampRequestHistory> getCampRequests() {
        return campRequests;
    }

    public void setCampRequests(ArrayList<CampRequestHistory> campRequests) {
        this.campRequests = campRequests;
    }
}
