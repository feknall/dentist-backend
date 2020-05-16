package ir.beheshti.dandun.base.socket;

public enum ChatStateType {
    SEARCHING("searching"), OPEN("open"), CLOSED("closed");

    private String value;
    ChatStateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
