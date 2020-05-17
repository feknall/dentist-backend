package ir.beheshti.dandun.base.user.util;

public enum DoctorStateType {
    NOT_ANSWERED("NOT_ANSWERED"), PENDING("PENDING"), ACTIVE("ACTIVE"), Rejected("REJECTED");

    String value;
    DoctorStateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
