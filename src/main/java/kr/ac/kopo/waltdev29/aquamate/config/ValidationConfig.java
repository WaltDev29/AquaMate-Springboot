package kr.ac.kopo.waltdev29.aquamate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Bean Validation 설정.
 * LocalValidatorFactoryBean 을 등록하여
 * @Valid / @Validated 어노테이션 기반 검증이 스프링 컨텍스트 안에서
 * 커스텀 Validator(@Password, @Phone)와 올바르게 동작하도록 연결.
 */
@Configuration
public class ValidationConfig {

    /**
     * 스프링의 Validator 구현체로 Hibernate Validator를 사용.
     * 커스텀 어노테이션(Password, Phone) 을 포함한 Bean Validation 처리.
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * 메서드 파라미터 수준의 @Validated 검증을 활성화.
     * Service/Repository 메서드 파라미터에도 @Valid 적용 가능.
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }
}
