package daryadelan.sandogh.zikey.com.daryadelan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Camp implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Camp createFromParcel(Parcel in) {
            return new Camp(in);
        }

        public Camp[] newArray(int size) {
            return new Camp[size];
        }
    };


    @SerializedName("campId")
    private long campID;
    @SerializedName("code")
    private long code;
    @SerializedName("campName")
    private String campName;
    @SerializedName("imagePath")
    private String imagePath;
    @SerializedName("reservable")
    private boolean reservable;
    @SerializedName("state")
    private String state;
    @SerializedName("city")
    private String city;
    @SerializedName("tel1")
    private String tel1;
    @SerializedName("tel2")
    private String tel2;
    @SerializedName("address")
    private String address;
    @SerializedName("description")
    private String description;
    @SerializedName("star")
    private int star;
    @SerializedName("galleryname")
    private String galleryName;
    @SerializedName("person")
    private ArrayList<CampReseption> campReseptions;

    public long getCampID() {
        return campID;
    }

    public void setCampID(long campID) {
        this.campID = campID;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isReservable() {
        return reservable;
    }

    public void setReservable(boolean reservable) {
        this.reservable = reservable;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public ArrayList<CampReseption> getCampReseptions() {
        return campReseptions;
    }

    public void setCampReseptions(ArrayList<CampReseption> campReseptions) {
        this.campReseptions = campReseptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.campID);
        dest.writeLong(this.code);
        dest.writeString(this.campName);
        dest.writeString(this.imagePath);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeString(this.tel1);
        dest.writeString(this.tel2);
        dest.writeString(this.address);
        dest.writeString(this.description);
        dest.writeString(this.galleryName);
        dest.writeInt(this.reservable ? 1 : 0);
        dest.writeInt(this.star);
    }

    public Camp(Parcel in) {
        this.campID = in.readLong();
        this.code = in.readLong();
        this.campName = in.readString();
        this.imagePath = in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.tel1 = in.readString();
        this.tel2 = in.readString();
        this.address = in.readString();
        this.description = in.readString();
        this.galleryName = in.readString();
        this.reservable = in.readInt() > 0 ? true : false;
        this.star = in.readInt();

    }
}
