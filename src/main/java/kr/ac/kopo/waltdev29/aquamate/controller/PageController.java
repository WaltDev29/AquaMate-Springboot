package kr.ac.kopo.waltdev29.aquamate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 모든 HTML 페이지 서빙을 담당하는 Controller.
 * 각 경로는 Thymeleaf 템플릿(templates/*.html)과 매핑.
 *
 * 기존 프론트엔드 파일 간 링크(.html 확장자 제거)와 매핑:
 *   index.html     → /
 *   signUp.html    → /signUp
 *   userHome.html  → /userHome
 *   dictionary.html → /dictionary
 *   fish_info.html → /fish_info
 *   match.html     → /match
 *   about.html     → /about
 */
@Controller
public class PageController {

    /** 사용자 홈 (새로운 진입점) */
    @GetMapping("/")
    public String userHome() {
        return "index";
    }

    /** 로그인 페이지 */
    @GetMapping("/login")
    public String index() {
        return "login";
    }

    /** 회원가입 페이지 */
    @GetMapping("/signUp")
    public String signUp() {
        return "signUp";
    }


    /** 물고기 도감 목록 */
    @GetMapping("/dictionary")
    public String dictionary() {
        return "dictionary";
    }

    /** 물고기 상세 정보 */
    @GetMapping("/fish_info")
    public String fishInfo() {
        return "fish_info";
    }

    /** 합사 매칭 시뮬레이터 */
    @GetMapping("/match")
    public String match() {
        return "match";
    }

    /** 서비스 소개 */
    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
