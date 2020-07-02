package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

public class SendMessage {

    @SerializedName("ConversationId")
    private long conversationId;
    @SerializedName("FileData")
    private String filesData;
    @SerializedName("MessageText")
    private String messageText;

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public String getFilesData() {
        return filesData;
    }

    public void setFilesData(String filesData) {
        this.filesData = filesData;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
