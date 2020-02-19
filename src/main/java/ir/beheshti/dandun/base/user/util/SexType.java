package ir.beheshti.dandun.base.user.util;

public enum SexType {
    Man(1), Woman(2);

    int value;

    SexType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
