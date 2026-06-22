package kr.ac.kopo.waltdev29.aquamate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * @Phone 어노테이션 실제 검증 로직.
 * 조건: 010-XXXX-XXXX 형식 (프론트엔드 signUp.js 의 phoneRegex 와 동일)
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    // 프론트엔드 signUp.js 의 phoneRegex 와 동일
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(010-\\d{4}-\\d{4})$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return PHONE_PATTERN.matcher(value).matches();
    }
}
