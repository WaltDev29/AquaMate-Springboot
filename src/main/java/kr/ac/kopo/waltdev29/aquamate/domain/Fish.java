package kr.ac.kopo.waltdev29.aquamate.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 물고기 정보 도메인 클래스.
 * data_fish.json 의 각 물고기 항목과 1:1 대응.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fish {

    /** JSON 키 (goldfish, betta ...) - 파싱 후 수동으로 세팅 */
    private String key;

    /** 번호 (001, 002 ...) */
    private String num;

    /** 한국어 이름 */
    private String name;

    /** 기본 정보 HTML 문자열 */
    private String basic;

    /** 사육 환경 HTML 문자열 */
    private String env;

    /** 품종 설명 */
    @JsonProperty("type")
    private String type;

    /** 사육 방법 */
    private String taming;

    /** 이미지 경로 (예: images/fish_gold.jpg) */
    private String img;
}
