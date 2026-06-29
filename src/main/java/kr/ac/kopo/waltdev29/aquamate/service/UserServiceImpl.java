package kr.ac.kopo.waltdev29.aquamate.service;

import kr.ac.kopo.waltdev29.aquamate.domain.User;
import kr.ac.kopo.waltdev29.aquamate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserService 구현체.
 * Repository 메서드를 위임하며 회원가입/로그인 비즈니스 로직 포함.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입 처리.
     * @return 중복 여부에 따라 상태 문자열 반환
     */
    @Override
    public String signUp(User user) {
        if (userRepository.existsById(user.getId())) {
            log.warn("[UserService] 중복 아이디 가입 시도: {}", user.getId());
            return "DUPLICATE_ID";
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("[UserService] 중복 이메일 가입 시도: {}", user.getEmail());
            return "DUPLICATE_EMAIL";
        }
        if (userRepository.existsByPhoneNum(user.getPhoneNum())) {
            log.warn("[UserService] 중복 전화번호 가입 시도: {}", user.getPhoneNum());
            return "DUPLICATE_PHONE";
        }
        userRepository.save(user);
        log.info("[UserService] 신규 회원 가입 완료: {}", user.getId());
        return "SUCCESS";
    }

    /**
     * 로그인 검증.
     * @return 아이디·비밀번호 일치 시 true
     */
    @Override
    public boolean login(String id, String pw) {
        return userRepository.findById(id)
                .map(user -> user.getPw().equals(pw))
                .orElse(false);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean addBookmark(String userId, String fishKey) {
        return userRepository.findById(userId).map(user -> user.getBookmarks().add(fishKey)).orElse(false);
    }

    @Override
    public boolean removeBookmark(String userId, String fishKey) {
        return userRepository.findById(userId).map(user -> user.getBookmarks().remove(fishKey)).orElse(false);
    }

    @Override
    public java.util.Set<String> getBookmarks(String userId) {
        return userRepository.findById(userId).map(User::getBookmarks).orElseGet(java.util.HashSet::new);
    }

    @Override
    public boolean updateUserInfo(String userId, User updatedUser) {
        return userRepository.findById(userId).map(user -> {
            updatedUser.setBookmarks(user.getBookmarks());
            userRepository.save(updatedUser);
            return true;
        }).orElse(false);
    }
}
