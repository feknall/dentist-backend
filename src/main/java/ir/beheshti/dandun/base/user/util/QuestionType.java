package ir.beheshti.dandun.base.user.util;

public enum QuestionType {
    Open(1), ZeroToFive(2), TrueFalse(3), MultipleChoice(4);

    int value;

    QuestionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
