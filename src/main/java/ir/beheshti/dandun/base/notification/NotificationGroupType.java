package ir.beheshti.dandun.base.notification;

public enum NotificationGroupType {
    PATIENT("PATIENT"), GREEN("GREEN"), YELLOW("YELLOW"), RED("RED"), DOCTOR("DOCTOR");

    private String value;
    NotificationGroupType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
