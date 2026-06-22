package kr.ac.kopo.waltdev29.aquamate.repository;

import kr.ac.kopo.waltdev29.aquamate.domain.Fish;

import java.util.List;
import java.util.Optional;

/**
 * 물고기 정보 조회를 위한 Repository 인터페이스.
 * 구현체(FishRepositoryImpl)에서 data_fish.json 을 파싱하여 제공.
 */
public interface FishRepository {

    /** 전체 물고기 목록 반환 */
    List<Fish> getFishList();

    /** JSON 키(예: "goldfish")로 단일 물고기 조회 */
    Optional<Fish> getFishByKey(String key);
}
