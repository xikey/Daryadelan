package daryadelan.sandogh.zikey.com.daryadelan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Photo implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @SerializedName("postID")
    private long galleryID;
    @SerializedName("galleryName")
    private String galleryName;
    @SerializedName("galleryNameFa")
    private String galleryNameFA;
    @SerializedName("galleryImage")
    private String galleryImage;
    @SerializedName("galleryThumbImage")
    private String galleryThumbImage;
    @SerializedName("createdat")
    private String createDate;
    @SerializedName("datefa")
    private String persianDate;

    public long getGalleryID() {
        return galleryID;
    }

    public void setGalleryID(long galleryID) {
        this.galleryID = galleryID;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public String getGalleryNameFA() {
        return galleryNameFA;
    }

    public void setGalleryNameFA(String galleryNameFA) {
        this.galleryNameFA = galleryNameFA;
    }

    public String getGalleryImage() {
        return galleryImage;
    }

    public void setGalleryImage(String galleryImage) {
        this.galleryImage = galleryImage;
    }

    public String getGalleryThumbImage() {
        return galleryThumbImage;
    }

    public void setGalleryThumbImage(String galleryThumbImage) {
        this.galleryThumbImage = galleryThumbImage;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPersianDate() {
        return persianDate;
    }

    public void setPersianDate(String persianDate) {
        this.persianDate = persianDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.galleryID);
        dest.writeString(this.galleryName);
        dest.writeString(this.galleryNameFA);
        dest.writeString(this.galleryImage);
        dest.writeString(this.galleryThumbImage);
        dest.writeString(this.createDate);
        dest.writeString(this.persianDate);

    }

    public Photo(Parcel in) {
        this.galleryID = in.readLong();
        this.galleryName = in.readString();
        this.galleryNameFA = in.readString();
        this.galleryImage = in.readString();
        this.galleryThumbImage = in.readString();
        this.createDate = in.readString();
        this.persianDate = in.readString();


    }
}
