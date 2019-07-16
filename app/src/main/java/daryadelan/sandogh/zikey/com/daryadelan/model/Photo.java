package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

public class Photo {

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
}
