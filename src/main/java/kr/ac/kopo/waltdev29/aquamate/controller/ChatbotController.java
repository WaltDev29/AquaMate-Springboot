package kr.ac.kopo.waltdev29.aquamate.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${llm.base.url:${LLM_BASE_URL:}}")
    private String llmBaseUrl;

    @Value("${llm.model:${LLM_MODEL:gpt-4o-mini}}")
    private String llmModel;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getCompletionsUrl(String customBaseUrl) {
        String base = (customBaseUrl != null && !customBaseUrl.trim().isEmpty())
                ? customBaseUrl.trim()
                : ((llmBaseUrl != null && !llmBaseUrl.trim().isEmpty()) ? llmBaseUrl.trim() : "https://api.openai.com/v1");
        base = base.replaceAll("/+$", "");
        if (base.endsWith("/chat/completions")) {
            return base;
        }
        return base + "/chat/completions";
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String authKey = (apiKey != null && !apiKey.trim().isEmpty()) ? apiKey : "APIKEY";
        headers.set("Authorization", "Bearer " + authKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", "Mozilla/5.0");
        return headers;
    }

    @PostMapping("/ask")
    public ResponseEntity<?> askChatbot(@RequestBody Map<String, Object> requestBody) {
        String customBaseUrl = requestBody.get("base_url") != null ? String.valueOf(requestBody.get("base_url")) : null;
        String url = getCompletionsUrl(customBaseUrl);

        Map<String, Object> payload = new HashMap<>(requestBody);
        payload.remove("base_url");
        if (!payload.containsKey("model") || payload.get("model") == null) {
            payload.put("model", llmModel);
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

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
        String customBaseUrl = body.get("base_url");

        if (fish1 == null || fish2 == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"fish1 and fish2 are required\"}");
        }

        String targetModel = body.getOrDefault("model", llmModel);
        String systemPrompt = "너는 수족관 합사 전문가야. 주어지는 두 물고기(fish1, fish2)의 합사 궁합을 0~100 사이의 퍼센트 수치와 3~4문장의 아주 구체적이고 상세한 평가 코멘트로 반환해. 수질, 성향, 크기 차이 등을 고려하여 이유를 명확히 설명해. 반드시 JSON 포맷으로 응답해. 예: {\"percentage\": 80, \"comment\": \"베타는 공격성이 있지만...\"}";
        String userPrompt = "fish1: " + fish1 + ", fish2: " + fish2;

        Map<String, Object> requestBody = Map.of(
                "model", targetModel,
                "response_format", Map.of("type", "json_object"),
                "messages", new Object[]{
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                }
        );

        String url = getCompletionsUrl(customBaseUrl);
        HttpHeaders headers = createHeaders();
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
