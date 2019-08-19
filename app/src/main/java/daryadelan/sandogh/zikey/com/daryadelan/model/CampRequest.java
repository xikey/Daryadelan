package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CampRequest {

    @SerializedName("PersonalCode")
    private long PersonalCode;
    @SerializedName("NationalCode")
    private long NationalCode;
    @SerializedName("Count")
    private long count;
    @SerializedName("Day")
    private long day;
    @SerializedName("Person")
    private ArrayList<CampReseption> campReseptions;
    @SerializedName("RequestDate")
    private String requestDate;
    @SerializedName("campId")
    private long campID;

    public long getPersonalCode() {
        return PersonalCode;
    }

    public void setPersonalCode(long personalCode) {
        PersonalCode = personalCode;
    }

    public long getNationalCode() {
        return NationalCode;
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
