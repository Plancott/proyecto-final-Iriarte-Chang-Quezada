package com.microservices.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Map;

@Component
public class OpenAiClient {

    private final WebClient webClient;
    private final String model;
    private final Duration timeout;

    public OpenAiClient(@Value("${openai.base-url}") String baseUrl,
                        @Value("${openai.api-key}") String apiKey,
                        @Value("${openai.model}") String model,
                        @Value("${openai.timeout-seconds}") long timeoutSeconds) {
        this.model = model;
        this.timeout = Duration.ofSeconds(timeoutSeconds);
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }


    public Mono<String> askCompletion(String prompt) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", new Object[]{Map.of("role", "user", "content", prompt)},
                "max_tokens", 500
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(timeout)
                .map(resp -> {
                    Object choices = resp.get("choices");
                    if (choices instanceof java.util.List && !((java.util.List) choices).isEmpty()) {
                        Map first = (Map) ((java.util.List) choices).get(0);
                        Map message = (Map) first.get("message");
                        return message != null ? (String) message.get("content") : resp.toString();
                    }
                    return resp.toString();
                });
    }

}
