package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class ServerWrapper {

    @SerializedName("resultId")
    @Expose
    private int ResultId = -1;
    @SerializedName("strData")
    @Expose
    private String StrData;
    @SerializedName("message")
    @Expose
    private String Messagee = null;
    @SerializedName("error_description")
    private String error_description;



    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

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
