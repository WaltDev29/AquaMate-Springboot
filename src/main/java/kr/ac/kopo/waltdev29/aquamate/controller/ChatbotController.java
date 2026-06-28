package kr.ac.kopo.waltdev29.aquamate.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/ask")
    public ResponseEntity<?> askChatbot(@RequestBody Map<String, Object> requestBody) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to call API: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/match")
    public ResponseEntity<?> matchFish(@RequestBody Map<String, String> body) {
        String fish1 = body.get("fish1");
        String fish2 = body.get("fish2");

        if (fish1 == null || fish2 == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"fish1 and fish2 are required\"}");
        }

        String systemPrompt = "너는 수족관 합사 전문가야. 주어지는 두 물고기(fish1, fish2)의 합사 궁합을 0~100 사이의 퍼센트 수치와 3~4문장의 아주 구체적이고 상세한 평가 코멘트로 반환해. 수질, 성향, 크기 차이 등을 고려하여 이유를 명확히 설명해. 반드시 JSON 포맷으로 응답해. 예: {\"percentage\": 80, \"comment\": \"베타는 공격성이 있지만...\"}";
        String userPrompt = "fish1: " + fish1 + ", fish2: " + fish2;

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "response_format", Map.of("type", "json_object"),
                "messages", new Object[]{
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                }
        );

        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to call API: " + e.getMessage() + "\"}");
        }
    }
}
