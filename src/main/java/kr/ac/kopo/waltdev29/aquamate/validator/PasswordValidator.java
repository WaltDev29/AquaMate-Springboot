package kr.ac.kopo.waltdev29.aquamate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * @Password 어노테이션 실제 검증 로직.
 * 조건: 영문/숫자/특수문자 조합 8~15자, 특수문자 최소 1개 포함, 한글·공백 불가.
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    // 프론트엔드 signUp.js 의 pwRegex 와 동일한 조건
    private static final Pattern PW_PATTERN =
            Pattern.compile("^(?=.*[^a-zA-Z0-9])[a-zA-Z0-9!@#$%^&*()_+~\\-={}\\[\\]:<>?,./]{8,15}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return PW_PATTERN.matcher(value).matches();
    }
}
