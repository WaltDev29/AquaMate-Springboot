package kr.ac.kopo.waltdev29.aquamate.repository;

import kr.ac.kopo.waltdev29.aquamate.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 인메모리(Map) 방식의 UserRepository 구현체.
 * DB 없이 서버 메모리에 사용자 정보를 저장.
 * (서버 재시작 시 데이터 초기화됨 - 추후 DB 연동 시 이 클래스만 교체)
 *
 * ConcurrentHashMap 사용 이유: 다중 요청 환경에서도 안전한 동시성 보장
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    // key: 사용자 아이디, value: User 객체
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        userStore.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return userStore.containsKey(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userStore.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByPhoneNum(String phoneNum) {
        return userStore.values().stream().anyMatch(user -> user.getPhoneNum().equals(phoneNum));
    }
}
