package kr.ac.kopo.waltdev29.aquamate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 전화번호 검증 어노테이션.
 * 형식: 010-XXXX-XXXX (프론트엔드 signUp.js 의 phoneRegex 와 동일)
 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
