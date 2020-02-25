package ir.beheshti.dandun.base.user.util;

public enum PatientStateType {
    UNCATEGORIZED(1), NORMAL(2), WARNING(3), DANGER(4);

    int value;
    PatientStateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
