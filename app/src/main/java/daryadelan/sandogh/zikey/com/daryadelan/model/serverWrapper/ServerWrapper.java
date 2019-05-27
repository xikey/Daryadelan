package daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper;


import com.google.gson.annotations.SerializedName;

public abstract class ServerWrapper {


    @SerializedName("ResultId")
    private int isSuccess = 0;
    @SerializedName("StrData")
    private String StrData ;
    @SerializedName("Message")
    private String message = null;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getStrData() {
        return StrData;
    }

    public void setStrData(String strData) {
        StrData = strData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
