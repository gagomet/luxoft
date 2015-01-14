package BankApplication.type;

/**
 * Created by Kir Kolesnikov on 14.01.2015.
 */
public enum Gender {
    MALE("Mr. "),
    FEMALE("Mrs. ");

    private final String genderPrefix;

    Gender(String prefix) {
        this.genderPrefix = prefix;
    }

    public String getGenderPrefix() {
        return genderPrefix;
    }
}
