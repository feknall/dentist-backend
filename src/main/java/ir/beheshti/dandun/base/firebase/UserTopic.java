package ir.beheshti.dandun.base.firebase;

public enum UserTopic {
    PATIENT("Patient"), GREEN("Green"), YELLOW("Yellow"), RED("Red");

    private String value;
    UserTopic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
