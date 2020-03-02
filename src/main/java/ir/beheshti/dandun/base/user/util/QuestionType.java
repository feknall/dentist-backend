package ir.beheshti.dandun.base.user.util;

public enum QuestionType {
    Open("Open"), SingleChoice("SingleChoice"), MultipleChoice("MultipleChoice"),
    Range("Range"), Image("Image");

    String value;

    QuestionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
