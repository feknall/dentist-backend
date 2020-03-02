package ir.beheshti.dandun.base.user.util;

public enum DoctorStateType {
    NOT_ANSWERED("NotAnswered"), ACTIVE("Active"), PENDING("Pending"), Rejected("Rejected");

    String value;
    DoctorStateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
