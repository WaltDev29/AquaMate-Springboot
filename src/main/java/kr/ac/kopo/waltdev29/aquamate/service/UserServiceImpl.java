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
     * @return 중복 아이디 없이 정상 저장 시 true, 중복 시 false
     */
    @Override
    public boolean signUp(User user) {
        if (userRepository.existsById(user.getId())) {
            log.warn("[UserService] 중복 아이디 가입 시도: {}", user.getId());
            return false;
        }
        userRepository.save(user);
        log.info("[UserService] 신규 회원 가입 완료: {}", user.getId());
        return true;
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
}
