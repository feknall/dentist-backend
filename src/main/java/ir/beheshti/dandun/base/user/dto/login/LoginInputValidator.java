package ir.beheshti.dandun.base.user.dto.login;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LoginInputValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginInputDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginInputDto loginInputDto = (LoginInputDto) target;
        if (loginInputDto.getPhoneNumber().equals("123"))
            errors.reject("phoneNumber", "123");
    }
}
