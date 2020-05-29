package ir.beheshti.dandun.base.socket;

public enum ChatMessageType {
    TEXT("TEXT"), IMAGE("IMAGE");

    String value;
    ChatMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
