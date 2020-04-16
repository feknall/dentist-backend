package ir.beheshti.dandun.base.firebase;

public enum MessageType {
    NOTIFICATION("notification"), CHAT("chat");

    private String value;
    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
