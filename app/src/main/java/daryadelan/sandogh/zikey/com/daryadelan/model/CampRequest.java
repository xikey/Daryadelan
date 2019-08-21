package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CampRequest {

    @SerializedName("personalCode")
    private long PersonalCode;
    @SerializedName("nationalCode")
    private long NationalCode;
    @SerializedName("count")
    private long count;
    @SerializedName("day")
    private long day;
    @SerializedName("subPersons")
    private ArrayList<CampReseption> campReseptions;
    @SerializedName("createdat")
    private String requestDate;
    @SerializedName("campId")
    private long campID;
    @SerializedName("campRequestId")
    private long campRequestID;

    @SerializedName("modifiedat")
    private String modifieDate;

    @SerializedName("requestState")
    private int requestState;

    @SerializedName("camp")
    private Camp camp;

    public int getRequestState() {
        return requestState;
    }

    public void setRequestState(int requestState) {
        this.requestState = requestState;
    }

    public long getPersonalCode() {
        return PersonalCode;
    }

    public void setPersonalCode(long personalCode) {
        PersonalCode = personalCode;
    }

    public long getNationalCode() {
        return NationalCode;
    }

    public long getCampRequestID() {
        return campRequestID;
    }

    public String getModifieDate() {
        return modifieDate;
    }

    public void setModifieDate(String modifieDate) {
        this.modifieDate = modifieDate;
    }

    public void setCampRequestID(long campRequestID) {
        this.campRequestID = campRequestID;
    }

    public void setNationalCode(long nationalCode) {
        NationalCode = nationalCode;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public Camp getCamp() {
        return camp;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
    }

    public ArrayList<CampReseption> getCampReseptions() {
        return campReseptions;
    }

    public void setCampReseptions(ArrayList<CampReseption> campReseptions) {
        this.campReseptions = campReseptions;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public long getCampID() {
        return campID;
    }

    public void setCampID(long campID) {
        this.campID = campID;
    }
}
