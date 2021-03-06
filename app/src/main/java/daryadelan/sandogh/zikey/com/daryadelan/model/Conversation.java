package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Conversation {

    @SerializedName("cid")
    private long conversationID;
    @SerializedName("uid")
    private long userID;
    @SerializedName("nmess")
    private boolean newMessage;
    @SerializedName("st")
    private long status;
    @SerializedName("stfa")
    private String statusPersianName;
    @SerializedName("subj")
    private String subject;
    @SerializedName("crat")
    private String createDate;
    @SerializedName("modf")
    private String modifyDate;
    @SerializedName("mess")
    private ArrayList<Message> messages;
    @SerializedName("mbrief")
    private ConversationResponseSummary response;





    public long getConversationID() {
        return conversationID;
    }

    public void setConversationID(long conversationID) {
        this.conversationID = conversationID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public boolean isNewMessage() {
        return newMessage;
    }

    public void setNewMessage(boolean newMessage) {
        this.newMessage = newMessage;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getStatusPersianName() {
        return statusPersianName;
    }

    public void setStatusPersianName(String statusPersianName) {
        this.statusPersianName = statusPersianName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ConversationResponseSummary getResponse() {
        return response;
    }

    public void setResponse(ConversationResponseSummary response) {
        this.response = response;
    }
}
