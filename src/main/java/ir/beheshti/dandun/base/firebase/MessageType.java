package ir.beheshti.dandun.base.firebase;

public enum MessageType {
    GENERAL("GENERAL"), CHAT("CHAT"), USER_STATE("USER_STATE");

    private String value;
    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
