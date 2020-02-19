package ir.beheshti.dandun.base.user.util;

public enum QuestionType {
    Open("Open"), TrueFalse("TrueFalse"), MultipleChoice("MultipleChoice");

    String value;

    QuestionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
