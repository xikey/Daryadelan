package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

public class Payroll {

    @SerializedName("year")
    private String year;
    @SerializedName("month")
    private String month;
    @SerializedName("payrollId")
    private long payrollId;
    @SerializedName("personalCode")
    private long personalCode;
    @SerializedName("amount")
    private long amount;
    @SerializedName("descCode")
    private long descCode;
    @SerializedName("mande")
    private long mande;
    @SerializedName("typePay")
    private long typePay;
    @SerializedName("transactionType")
    private long transactionType;
    @SerializedName("amountGhest")
    private long amountGhest;
    @SerializedName("payCode")
    private long payCode;
    @SerializedName("desc")
    private String desc;
    @SerializedName("fileName")
    private String fileName;
    @SerializedName("byteData")
    private String byteData;



    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(long payrollId) {
        this.payrollId = payrollId;
    }

    public long getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(long personalCode) {
        this.personalCode = personalCode;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getDescCode() {
        return descCode;
    }

    public void setDescCode(long descCode) {
        this.descCode = descCode;
    }

    public long getMande() {
        return mande;
    }

    public void setMande(long mande) {
        this.mande = mande;
    }

    public long getTypePay() {
        return typePay;
    }

    public void setTypePay(long typePay) {
        this.typePay = typePay;
    }

    public long getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(long transactionType) {
        this.transactionType = transactionType;
    }

    public long getAmountGhest() {
        return amountGhest;
    }

    public void setAmountGhest(long amountGhest) {
        this.amountGhest = amountGhest;
    }

    public long getPayCode() {
        return payCode;
    }

    public void setPayCode(long payCode) {
        this.payCode = payCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getByteData() {
        return byteData;
    }

    public void setByteData(String byteData) {
        this.byteData = byteData;
    }

    public String getMonthAsString()  {

        long month= Long.parseLong(getMonth());

        if (month == 0 || month > 12)
            return "ماه نا معتبر";
        if (month == 1)
            return "فروردین";
        if (month == 2)
            return "اردیبهشت";
        if (month == 3)
            return "خرداد";
        if (month == 4)
            return "تیر";
        if (month == 5)
            return "مرداد";
        if (month == 6)
            return "شهریور";
        if (month == 7)
            return "مهر";
        if (month == 8)
            return "آبان";
        if (month == 9)
            return "آذر";
        if (month == 10)
            return "دی";
        if (month == 11)
            return "بهمن";
        if (month == 12)
            return "اسفند";

        return "ماه نا معتبر";

    }
}


