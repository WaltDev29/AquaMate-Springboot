package kr.ac.kopo.waltdev29.aquamate.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kopo.waltdev29.aquamate.domain.Fish;
import kr.ac.kopo.waltdev29.aquamate.domain.User;
import kr.ac.kopo.waltdev29.aquamate.service.FishService;
import kr.ac.kopo.waltdev29.aquamate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 사용자 행동(로그인, 회원가입) 및 데이터 API(물고기 목록) 처리 Controller.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FishService fishService;

    // ─────────────────────────────────────────────
    // 회원가입 처리
    // ─────────────────────────────────────────────

    /**
     * POST /signUp - 회원가입 폼 제출 처리.
     * @Valid + BindingResult 로 서버 측 검증.
     * 비밀번호 불일치 검사 후 중복 아이디 확인.
     */
    @PostMapping("/signUp")
    public String signUp(@Valid @ModelAttribute User user,
                         BindingResult bindingResult,
                         Model model) {

        // 비밀번호 확인 불일치 검사
        if (user.getPwCheck() != null && !user.getPw().equals(user.getPwCheck())) {
            bindingResult.rejectValue("pwCheck", "mismatch", "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            log.warn("[UserController] 회원가입 유효성 검사 실패: {}", bindingResult.getAllErrors());
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "signUp"; // 다시 폼으로
        }

        boolean success = userService.signUp(user);
        if (!success) {
            model.addAttribute("errorMsg", "이미 사용 중인 아이디입니다.");
            return "signUp";
        }

        // 가입 성공 → 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }

    // ─────────────────────────────────────────────
    // 로그인 처리
    // ─────────────────────────────────────────────

    /**
     * POST /login - 로그인 폼 제출 처리.
     * 성공 시 세션에 userId 저장 후 메인 페이지(/)로 리다이렉트.
     */
    @PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String pw,
                        HttpSession session,
                        Model model) {

        boolean valid = userService.login(id, pw);
        if (!valid) {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }

        session.setAttribute("userId", id);
        log.info("[UserController] 로그인 성공: {}", id);
        return "redirect:/";
    }

    /** GET /logout - 세션 무효화 후 메인 페이지로 */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ─────────────────────────────────────────────
    // Fish 데이터 API (기존 fetch("data/data_fish.json") 대체)
    // ─────────────────────────────────────────────

    /**
     * GET /api/fish - 전체 물고기 목록을 key→Fish 형태의 Map(JSON 객체)으로 반환.
     * 기존 JS: Object.keys(data) 패턴을 그대로 유지하기 위해 배열 대신 Map 반환.
     * 예: { "goldfish": {...}, "betta": {...} }
     */
    @GetMapping("/api/fish")
    @ResponseBody
    public ResponseEntity<Map<String, Fish>> getFishList() {
        List<Fish> fishList = fishService.getFishList();
        // 순서를 보장하는 LinkedHashMap 으로 변환
        Map<String, Fish> fishMap = new LinkedHashMap<>();
        fishList.forEach(fish -> fishMap.put(fish.getKey(), fish));
        return ResponseEntity.ok(fishMap);
    }

    /**
     * GET /api/fish/{key} - 특정 물고기 상세 정보 JSON 반환.
     * 기존 JS: fish_info.js 에서 fishkey 파라미터로 단건 조회.
     */
    @GetMapping("/api/fish/{key}")
    @ResponseBody
    public ResponseEntity<?> getFishByKey(@PathVariable String key) {
        return fishService.getFishByKey(key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
