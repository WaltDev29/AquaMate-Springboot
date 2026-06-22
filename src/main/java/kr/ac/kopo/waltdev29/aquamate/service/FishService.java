package kr.ac.kopo.waltdev29.aquamate.service;

import kr.ac.kopo.waltdev29.aquamate.domain.Fish;

import java.util.List;
import java.util.Optional;

/**
 * 물고기 정보 서비스 인터페이스.
 * Repository 계층을 Controller에 노출하는 중간 계층.
 */
public interface FishService {

    List<Fish> getFishList();

    Optional<Fish> getFishByKey(String key);
}
