package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.model.Conversation;

public class ConversationWrapper extends ServerWrapper {

    @SerializedName("data")
    private ArrayList<Conversation> data;

    public ArrayList<Conversation> getData() {
        return data;
    }

    public void setData(ArrayList<Conversation> data) {
        this.data = data;
    }
}
