package kr.ac.kopo.waltdev29.aquamate.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.ac.kopo.waltdev29.aquamate.validator.Password;
import kr.ac.kopo.waltdev29.aquamate.validator.Phone;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 사용자 계정 도메인 클래스.
 * 프론트엔드 signUp.html 폼 필드와 1:1 대응.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 30, message = "이름은 2~30자 사이여야 합니다.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 80)
    private String email;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Phone
    private String phoneNum;

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(max = 15, message = "아이디는 최대 15자입니다.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Password
    private String pw;

    // 비밀번호 확인용 (DB 저장 불필요, 검증 목적)
    private String pwCheck;
}
