package kr.ac.kopo.waltdev29.aquamate.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import kr.ac.kopo.waltdev29.aquamate.domain.Fish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * data_fish.json 을 파싱하여 인메모리 Map에 보관하는 FishRepository 구현체.
 * 서버 기동 시(@PostConstruct) JSON을 한 번만 읽어 캐싱 → 반복 I/O 방지.
 */
@Slf4j
@Repository
public class FishRepositoryImpl implements FishRepository {

    // key: JSON 키 (goldfish, betta ...), value: Fish 객체
    private final Map<String, Fish> fishMap = new LinkedHashMap<>();

    /**
     * 서버 기동 시 classpath:data/data_fish.json 을 파싱하여 fishMap에 로딩.
     * LinkedHashMap 사용으로 JSON 순서 유지.
     */
    @PostConstruct
    public void loadFishData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("data/data_fish.json");
            InputStream is = resource.getInputStream();

            // JSON 최상위가 {key: FishObject, ...} 구조이므로 Map으로 파싱
            Map<String, Map<String, String>> rawMap =
                    mapper.readValue(is, new TypeReference<Map<String, Map<String, String>>>() {});

            rawMap.forEach((key, val) -> {
                Fish fish = new Fish();
                fish.setKey(key);
                fish.setNum(val.get("num"));
                fish.setName(val.get("name"));
                fish.setBasic(val.get("basic"));
                fish.setEnv(val.get("env"));
                fish.setType(val.get("type"));
                fish.setTaming(val.get("taming"));
                fish.setImg(val.get("img"));
                fishMap.put(key, fish);
            });

            log.info("[FishRepository] data_fish.json 로딩 완료 - 총 {}종", fishMap.size());
        } catch (IOException e) {
            // JSON 로딩 실패 시 서버 기동 자체를 중단하지 않고 경고만 출력
            log.error("[FishRepository] data_fish.json 로딩 실패: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<Fish> getFishList() {
        return new ArrayList<>(fishMap.values());
    }

    @Override
    public Optional<Fish> getFishByKey(String key) {
        return Optional.ofNullable(fishMap.get(key));
    }
}
