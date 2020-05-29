package ir.beheshti.dandun.base.firebase;


public enum NotificationParameter {
    SOUND("default"),
    COLOR("#00cbcf");

    private String value;

    NotificationParameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
