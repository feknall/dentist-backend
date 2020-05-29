package ir.beheshti.dandun.base.notification;

public enum NotificationGroupType {
    PATIENT("Patient"), GREEN("Green"), YELLOW("Yellow"), RED("Red"), DOCTOR("Doctor");

    private String value;
    NotificationGroupType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
