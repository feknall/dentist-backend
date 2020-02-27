package ir.beheshti.dandun.base.user.common;

public abstract class ErrorCodeAndMessage {
    public static final int QUESTION_NOT_FOUND_CODE = 7000;
    public static final String QUESTION_NOT_FOUND_MESSAGE = "question not found";

    public static final int SOME_ANSWERS_DONT_BELONG_TO_THIS_QUESTION_CODE = 7001;
    public static final String SOME_ANSWERS_DONT_BELONG_TO_THIS_QUESTION_MESSAGE = "some answers don't belong to this question";

    public static final int USER_ALREADY_ANSWERED_TO_THIS_QUESTION_CODE = 7002;
    public static final String USER_ALREADY_ANSWERED_TO_THIS_QUESTION_MESSAGE = "user already has answered to this questions";

    public static final int QUESTION_TYPE_DOESNT_MATCH_CODE = 7003;
    public static final String QUESTION_TYPE_DOESNT_MATCH_MESSAGE = "question with this type not found";

    public static final int USER_NOT_FOUND_CODE = 7004;
    public static final String USER_NOT_FOUND_MESSAGE = "user not found";

    public static final int PHONE_NUMBER_NOT_FOUND_CODE = 1007;
    public static final String PHONE_NUMBER_NOT_FOUND_MESSAGE = "phone number not found";

    public static final int PHONE_NUBMER_IS_NOT_VERIFIED_CODE = 1009;
    public static final String PHONE_NUBMER_IS_NOT_VERIFIED_MESSAGE = "phone number isn't verified";

    public static final int PHONE_NUMBER_ALREADY_SINGED_UP_CODE = 1008;
    public static final String PHONE_NUMBER_ALREADY_SINGED_UP_MESSAGE = "phone number already signed up";

    public static final int USER_DOES_NOT_HAVE_PHOTO_CODE = 7005;
    public static final String USER_DOES_NOT_HAVE_PHOTO_MESSAGE = "user doesn't have photo";


    public static final int INTERNAL_SERVER_ERROR_CODE = 1000;
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal server error";
}
