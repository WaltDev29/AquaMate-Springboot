package kr.ac.kopo.waltdev29.aquamate.repository;

import kr.ac.kopo.waltdev29.aquamate.domain.User;

import java.util.Optional;

/**
 * 사용자 계정 저장소 인터페이스.
 * 현재 구현체(UserRepositoryImpl)는 인메모리 Map 방식.
 */
public interface UserRepository {

    /** 사용자 저장 */
    void save(User user);

    /** 아이디(id)로 사용자 조회 */
    Optional<User> findById(String id);

    /** 아이디 중복 여부 확인 */
    boolean existsById(String id);

    /** 이메일 중복 여부 확인 */
    boolean existsByEmail(String email);

    /** 전화번호 중복 여부 확인 */
    boolean existsByPhoneNum(String phoneNum);
}
