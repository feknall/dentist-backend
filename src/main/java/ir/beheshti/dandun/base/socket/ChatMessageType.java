package ir.beheshti.dandun.base.socket;

public enum ChatMessageType {
    TEXT("test"), IMAGE("image");

    String value;
    ChatMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
