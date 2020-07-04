package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.ServerWrapper;

public class ConversationTopic  {

    @SerializedName("Subject")
    private String subject;
    @SerializedName("MessageText")
    private String messageText;



    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
