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
    private boolean fus;
    @SerializedName("pmid")
    private long pmid;
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

    public boolean isFus() {
        return fus;
    }

    public void setFus(boolean fus) {
        this.fus = fus;
    }

    public long getPmid() {
        return pmid;
    }

    public void setPmid(long pmid) {
        this.pmid = pmid;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
