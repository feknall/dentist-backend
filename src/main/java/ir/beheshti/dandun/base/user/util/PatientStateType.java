package ir.beheshti.dandun.base.user.util;

public enum PatientStateType {
    UN_CATEGORIZED(1), GREEN(2), YELLOW(3), RED(4);

    int value;
    PatientStateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
