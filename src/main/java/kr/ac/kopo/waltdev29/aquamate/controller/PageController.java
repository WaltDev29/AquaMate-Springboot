package kr.ac.kopo.waltdev29.aquamate.controller;

import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.waltdev29.aquamate.domain.User;
import kr.ac.kopo.waltdev29.aquamate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

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
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;

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

    /** 마이페이지 */
    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "myPage";
        } else {
            session.invalidate();
            return "redirect:/login";
        }
    }
}
