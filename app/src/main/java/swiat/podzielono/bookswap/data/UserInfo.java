package swiat.podzielono.bookswap.data;

public class UserInfo {


    private String residence;
    private String study;
    private String field_of_study;

    public UserInfo(String residence, String study, String field_of_study) {
        this.residence = residence;
        this.study = study;
        this.field_of_study = field_of_study;
    }

    public UserInfo() {}

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getField_of_study() {
        return field_of_study;
    }

    public void setField_of_study(String field_of_study) {
        this.field_of_study = field_of_study;
    }
}
