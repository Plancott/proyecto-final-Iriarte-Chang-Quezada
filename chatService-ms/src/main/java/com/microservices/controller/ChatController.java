package com.microservices.controller;

import com.microservices.model.dto.ChatRequestDto;
import com.microservices.model.dto.ChatResponseDto;
import com.microservices.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chatAI")
public class ChatController {
    private final ChatService service;
    public ChatController(ChatService service) {
        this.service = service;
    }

    @PostMapping("/analizar")
    public Mono<ResponseEntity<ChatResponseDto>> analizar(@Valid @RequestBody ChatRequestDto request) {
        return service.recommend(request)
                .map(ResponseEntity::ok);
    }
}
