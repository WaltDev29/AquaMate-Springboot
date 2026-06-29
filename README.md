# 🐟 AquaMate

<p align="center">
<img src="https://github.com/user-attachments/assets/4a86264f-dca8-4abd-92b0-2df33aae3c03" width="500">
</p>


### "이 물고기랑 합사해도 될까?"
물생활을 시작한 사람이라면 한 번쯤 고민해봤을 질문입니다.  
이 사이트는 그런 고민을 덜어드리기 위해 만들어졌습니다.  
AquaMate는 물고기 사전과 두 물고기 간의 합사 궁합을 분석해주는 기능을 제공하는 웹 어플리케이션입니다. 

## 🔍 사이트 접속
👉 [**Click Here!**](https://nextwave.aikopo.net/aquamate/)


## 🛠️ Tech Stack
- **Backend**: Java 17, Spring Boot, Spring WebMVC
- **Frontend**: HTML5, CSS3, JavaScript, jQuery, Thymeleaf
- **Data Management**: In-Memory
- **Etc**: Lombok, Jakarta Validation, Jackson JSON Parsing


## 📌 주요 기능
- **물고기 사전 (Dictionary)**: 다양한 관상어의 상세 정보를 확인할 수 있으며 즐겨찾기 기능을 지원합니다.
- **합사 시뮬레이터 (Match)**: 두 물고기를 선택하여 같은 어항에서 함께 키울 수 있는지 궁합을 분석해 줍니다.
- **AI 헬프 챗봇 (Help Bot)**: 물고기와 관련된 질문이나 웹사이트 이용 방법에 대해 실시간으로 답변해 주는 화면 우측 하단의 플로팅 챗봇입니다.
- **개인화 (My Page)**: 즐겨찾기한 물고기들을 모아보고 내 정보를 관리할 수 있습니다.


## 📷 사진
| 기능 | 동작 이미지 |
| :--- | :--- |
| **물고기 사전 (Dictionary)** | <img width="640" alt="image" src="https://github.com/user-attachments/assets/803e5e94-d33b-4559-8e57-073321b55bae" />|
| **합사 시뮬레이터 (Match)** | <img width="640" alt="image" src="https://github.com/user-attachments/assets/3c24c2b9-c49a-4d29-a5be-4cfb211d19ef" />|
| **AI 헬프 챗봇 (Help Bot)** | <img width="640" alt="스크린샷 2026-06-29 185554" src="https://github.com/user-attachments/assets/c15456b2-e668-44a6-832f-89221a29d62f" />|


## ⚙️ 프로젝트 아키텍처 특징
- **인메모리 데이터 스토어**: 현재 별도의 DB 연동 없이 인메모리를 활용해 데이터를 캐싱 및 저장하고 있습니다. 
- **데이터 로딩 최적화**: 서버 기동 시 `@PostConstruct`를 활용해 물고기 데이터 JSON 파일을 단 한 번만 파싱하여 메모리에 적재함으로써, 반복적인 디스크 I/O를 방지하고 빠른 응답 속도를 보장합니다.
- **세션 기반 인증**: 자체 커스텀 검증 로직 및 로그인 성공 시 `HttpSession`에 사용자 ID를 발급하여 세션을 유지하고 개인화 기능(마이페이지, 북마크)을 제공합니다.
- **입력값 검증**: 도메인 객체에 `@Valid`, `@NotBlank`, 커스텀 정규식 어노테이션 등을 적용하여 컨트롤러 단에서 `BindingResult`로 유효성을 검사하고 에러 뷰를 직관적으로 처리합니다.


## 🤝 기여하기
버그 리포트나 기능 제안은 Issue 탭을 통해 언제든지 환영합니다!  
