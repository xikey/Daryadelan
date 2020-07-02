package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("mid")
    private long messageID;
    @SerializedName("muid")
    private long messageUserId;
    @SerializedName("name")
    private String userName;
    @SerializedName("mcrat")
    private String messageCreateAt;
    @SerializedName("fpth")
    private String messageFilePath;
    @SerializedName("messtxt")
    private String messageText;
    @SerializedName("fus")
    private boolean fromUser;
    @SerializedName("pmid")
    private long parentID;
    @SerializedName("rd")
    private boolean read;


    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public long getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(long messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageCreateAt() {
        return messageCreateAt;
    }

    public void setMessageCreateAt(String messageCreateAt) {
        this.messageCreateAt = messageCreateAt;
    }

    public String getMessageFilePath() {
        return messageFilePath;
    }

    public void setMessageFilePath(String messageFilePath) {
        this.messageFilePath = messageFilePath;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public boolean isFromUser() {
        return fromUser;
    }

    public void setFromUser(boolean fromUser) {
        this.fromUser = fromUser;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }


}
