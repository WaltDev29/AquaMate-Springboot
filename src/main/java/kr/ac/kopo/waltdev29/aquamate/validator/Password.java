package kr.ac.kopo.waltdev29.aquamate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 비밀번호 검증 어노테이션.
 * 영문/숫자/특수문자 조합 8~15자리, 특수문자 최소 1개 포함, 한글·공백 제외.
 * (프론트엔드 signUp.js 의 pwRegex 조건과 동일)
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "비밀번호는 특수문자를 포함한 영문/숫자 8~15자리여야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
