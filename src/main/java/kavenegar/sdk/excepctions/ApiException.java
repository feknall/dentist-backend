/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kavenegar.sdk.excepctions;


import kavenegar.sdk.enums.MetaData;

/**
 *
 * @author mohsen
 */
public class ApiException extends BaseException {

    int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public MetaData getCode() {
        return MetaData.valueOf(code);
    }
}
