package ir.beheshti.dandun.base.user.util;

public enum UserType {
    Patient(1), Doctor(2), Operator(3);

    int value;
    UserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
