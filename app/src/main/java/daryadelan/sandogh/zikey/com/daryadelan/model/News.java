package daryadelan.sandogh.zikey.com.daryadelan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class News  implements Parcelable {


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @SerializedName("postID")
    private long postID;
    @SerializedName("postTitle")
    private String postTitle;
    @SerializedName("postBody")
    private String postBody;
    @SerializedName("postSummary")
    private String postSummary;
    @SerializedName("postThumbImage")
    private String postThumbImage;
    @SerializedName("createdat")
    private String createDate;
    @SerializedName("postImage")
    private String postImage;
    @SerializedName("datefa")
    private String persianDate;


    public long getPostID() {
        return postID;
    }

    public void setPostID(long postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String getPostSummary() {
        return postSummary;
    }

    public void setPostSummary(String postSummary) {
        this.postSummary = postSummary;
    }

    public String getPostThumbImage() {
        return postThumbImage;
    }

    public void setPostThumbImage(String postThumbImage) {
        this.postThumbImage = postThumbImage;
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

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.postID);
        dest.writeString(this.postTitle);
        dest.writeString(this.postBody);
        dest.writeString(this.postSummary);
        dest.writeString(this.postThumbImage);
        dest.writeString(this.createDate);
        dest.writeString(this.postImage);
        dest.writeString(this.persianDate);
    }

    public News(Parcel in){
        this.postID = in.readLong();
        this.postTitle = in.readString();
        this.postBody =  in.readString();
        this.postSummary =  in.readString();
        this.postThumbImage =  in.readString();
        this.createDate =  in.readString();
        this.postImage =  in.readString();
        this.persianDate =  in.readString();

    }
}
