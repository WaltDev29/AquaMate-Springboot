package kr.ac.kopo.waltdev29.aquamate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 정적 리소스 경로 설정.
 * 스프링 부트 기본 설정(classpath:/static/) 에 추가로
 * 이미지 파일을 /images/** 경로로 명시적으로 서빙.
 *
 * 기본 설정으로도 static/ 하위 리소스는 자동 서빙되지만,
 * 명시적으로 등록하여 향후 외부 경로 연동 시 이 클래스만 수정하면 됨.
 */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // CSS, JS, 이미지 등 모든 정적 리소스를 classpath:/static/ 에서 서빙
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        registry.addResourceHandler("/data/**")
                .addResourceLocations("classpath:/static/data/");
    }
}
