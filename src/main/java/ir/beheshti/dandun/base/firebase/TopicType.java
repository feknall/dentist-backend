package ir.beheshti.dandun.base.firebase;

public enum TopicType {
    PATIENT("Patient"), GREEN("Green"), YELLOW("Yellow"), RED("Red"), DOCTOR("Doctor");

    private String value;
    TopicType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
