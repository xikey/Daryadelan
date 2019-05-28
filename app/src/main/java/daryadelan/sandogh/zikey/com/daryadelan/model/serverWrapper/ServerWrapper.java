package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerWrapper {

    @SerializedName("ResultId")
    @Expose
    private int ResultId = -1;
    @SerializedName("StrData")
    @Expose
    private String StrData;
    @SerializedName("Message")
    @Expose
    private String Messagee = null;



    public int getResultId() {
        return ResultId;
    }

    public void setResultId(int resultId) {
        this.ResultId = resultId;
    }

    public String getStrData() {
        return StrData;
    }

    public void setStrData(String strData) {
        StrData = strData;
    }

    public String getMessagee() {
        return Messagee;
    }

    public void setMessagee(String messagee) {
        this.Messagee = messagee;
    }

    public ServerWrapper() {

    }

}
