package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

public class ConversationTopicWrapper extends ServerWrapper {

    @SerializedName("data")
    private long data;

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
