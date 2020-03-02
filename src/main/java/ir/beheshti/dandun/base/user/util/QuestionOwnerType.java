package ir.beheshti.dandun.base.user.util;

public enum QuestionOwnerType {
    Patient("Patient"), Doctor("Doctor"), Public("Public");

    String value;

    QuestionOwnerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
