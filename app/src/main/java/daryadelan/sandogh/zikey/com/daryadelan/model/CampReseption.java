package daryadelan.sandogh.zikey.com.daryadelan.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CampReseption {

    private long id;
    @SerializedName("firstName")
    private String name;
    @SerializedName("lastName")
    private String family;
    @SerializedName("nationalCode")
    private long nationalCode;
    @SerializedName("nesbat")
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

    public ArrayList<CampReseption> getAllAvailableRelations() {
        ArrayList<CampReseption> campReseptions = new ArrayList<>();

        CampReseption camp0 = new CampReseption();
        camp0.setRelation(0);
        campReseptions.add(camp0);

        CampReseption camp1 = new CampReseption();
        camp1.setRelation(1);
        campReseptions.add(camp1);

        CampReseption camp2 = new CampReseption();
        camp2.setRelation(2);
        campReseptions.add(camp2);

        CampReseption camp3 = new CampReseption();
        camp3.setRelation(3);
        campReseptions.add(camp3);


        return campReseptions;

    }
}
