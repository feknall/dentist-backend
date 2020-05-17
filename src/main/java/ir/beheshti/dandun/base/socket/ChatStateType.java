package ir.beheshti.dandun.base.socket;

public enum ChatStateType {
    SEARCHING("SEARCHING"), OPEN("OPEN"), CLOSED("CLOSED");

    private String value;
    ChatStateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
