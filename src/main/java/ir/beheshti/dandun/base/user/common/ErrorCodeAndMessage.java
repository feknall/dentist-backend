package ir.beheshti.dandun.base.user.common;

public abstract class ErrorCodeAndMessage {
    public static final int QUESTION_NOT_FOUND_CODE = 7000;
    public static final String QUESTION_NOT_FOUND_MESSAGE = "question not found";

    public static final int SOME_ANSWERS_DONT_BELONG_TO_THIS_QUESTION_CODE = 7001;
    public static final String SOME_ANSWERS_DONT_BELONG_TO_THIS_QUESTION_MESSAGE = "some answers don't belong to this question";

    public static final int USER_ALREADY_ANSWERED_TO_THIS_QUESTION_CODE = 7002;
    public static final String USER_ALREADY_ANSWERED_TO_THIS_QUESTION_MESSAGE = "user already has answered to this questions";
}
