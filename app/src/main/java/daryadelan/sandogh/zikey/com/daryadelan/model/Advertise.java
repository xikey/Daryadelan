package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

public class Advertise {

    @SerializedName("galleryItemId")
    private long galleryItemId;
    @SerializedName("itemPath")
    private String imagepath;
    @SerializedName("modifiedat")
    private String modifiedat;


    public long getGalleryItemId() {
        return galleryItemId;
    }

    public void setGalleryItemId(long galleryItemId) {
        this.galleryItemId = galleryItemId;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getModifiedat() {
        return modifiedat;
    }

    public void setModifiedat(String modifiedat) {
        this.modifiedat = modifiedat;
    }
}
