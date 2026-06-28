package kr.ac.kopo.waltdev29.aquamate.service;

import kr.ac.kopo.waltdev29.aquamate.domain.User;

import java.util.Optional;

/**
 * 사용자 계정 서비스 인터페이스.
 */
public interface UserService {

    /** 회원가입: 중복 아이디 검사 후 저장. 중복 시 false 반환 */
    boolean signUp(User user);

    /** 로그인: 아이디/비밀번호 일치 여부 확인 */
    boolean login(String id, String pw);

    Optional<User> findById(String id);

    boolean addBookmark(String userId, String fishKey);
    boolean removeBookmark(String userId, String fishKey);
    java.util.Set<String> getBookmarks(String userId);

    boolean updateUserInfo(String userId, User updatedUser);
}
