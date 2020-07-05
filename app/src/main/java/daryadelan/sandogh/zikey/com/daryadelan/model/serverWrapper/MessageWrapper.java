package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;

import com.google.gson.annotations.SerializedName;

public class MessageWrapper extends ServerWrapper {

    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
