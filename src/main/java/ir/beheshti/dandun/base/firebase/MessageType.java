package ir.beheshti.dandun.base.firebase;

public enum MessageType {
    GENERAL("general"), CHAT("chat"), USER_STATE("userState");

    private String value;
    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
