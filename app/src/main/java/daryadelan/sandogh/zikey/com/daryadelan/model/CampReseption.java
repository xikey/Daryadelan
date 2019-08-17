package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

public class CampReseption {

    private long id;
    @SerializedName("FirstName")
    private String name;
    @SerializedName("LastName")
    private String family;
    @SerializedName("NationalCode")
    private long nationalCode;
    @SerializedName("Nesbat")
    private int relation;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public long getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(long nationalCode) {
        this.nationalCode = nationalCode;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRelationShipName() {

        if (relation == 0) {
            return "خودم";
        }
        if (relation == 1) {
            return "بازنشسته,موظف و تحت تکفل";
        }
        if (relation == 2) {
            return "اقوام درجه 1";
        }

        return "اقوام درجه 2";
    }
}
