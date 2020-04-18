package ir.beheshti.dandun.base.user.util;

public enum MessageDirectionType {
    SENDER("sender"), RECEIVER("receiver");

    String value;
    MessageDirectionType(String value) {
        this.value = value;
    }
}
