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

    public static final int ANSWER_IMAGE_NOT_FOUND_CODE = 7005;
    public static final String ANSWER_IMAGE_NOT_FOUND_MESSAGE = "image not found";

    public static final int QUESTION_OWNER_DOESNT_MATCH_CODE = 7006;
    public static final String QUESTION_OWNER_DOESNT_MATCH_MESSAGE = "question with this owner not found";

    public static final int QUESTION_OWNER_NOT_ALLOWED_CODE = 7007;
    public static final String QUESTION_OWNER_NOT_ALLOWED_MESSAGE = "owner type not found";

    public static final int USER_DOES_NOT_HAVE_PHOTO_CODE = 7008;
    public static final String USER_DOES_NOT_HAVE_PHOTO_MESSAGE = "user doesn't have photo";

    public static final int INFORMATION_NOT_FOUND_CODE = 7009;
    public static final String INFORMATION_NOT_FOUND_MESSAGE = "information not found";

    public static final int ANSWER_NOT_FOUND_CODE = 7010;
    public static final String ANSWER_NOT_FOUND_MESSAGE = "answer not found";

    public static final int NOTIFICATION_NOT_FOUND_CODE = 7011;
    public static final String NOTIFICATION_NOT_FOUND_MESSAGE = "notification not found";

    public static final int FAQ_NOT_FOUND_CODE = 7012;
    public static final String FAQ_NOT_FOUND_MESSAGE = "faq not found";

    public static final int PASSWORD_IS_WRONG_CODE = 7005;
    public static final String PASSWORD_IS_WRONG_MESSAGE = "password is wrong";

    public static final int PHONE_NUMBER_NOT_FOUND_CODE = 1007;
    public static final String PHONE_NUMBER_NOT_FOUND_MESSAGE = "phone number not found";

    public static final int PHONE_NUMBER_IS_NOT_VERIFIED_CODE = 1009;
    public static final String PHONE_NUMBER_IS_NOT_VERIFIED_MESSAGE = "phone number isn't verified";

    public static final int PHONE_NUMBER_ALREADY_SINGED_UP_CODE = 1008;
    public static final String PHONE_NUMBER_ALREADY_SINGED_UP_MESSAGE = "phone number already signed up";

    public static final int INTERNAL_SERVER_ERROR_CODE = 1000;
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal server error";
}
