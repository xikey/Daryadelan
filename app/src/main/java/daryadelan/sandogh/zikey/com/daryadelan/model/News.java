package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

public class News {

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
    @SerializedName("createDate")
    private String createDate;


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
}
