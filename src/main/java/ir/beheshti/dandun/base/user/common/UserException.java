package ir.beheshti.dandun.base.user.common;

import lombok.Data;

@Data
public class UserException extends RuntimeException {
    private int code;
    private String message;
    private boolean ok;
    public UserException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public UserException(int code, String message, boolean ok) {
        this.code = code;
        this.message = message;
        this.ok = ok;
    }
}
