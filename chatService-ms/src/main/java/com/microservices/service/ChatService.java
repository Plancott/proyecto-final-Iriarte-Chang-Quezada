package com.microservices.service;

import com.microservices.model.dto.ChatRequestDto;
import com.microservices.model.dto.ChatResponseDto;
import reactor.core.publisher.Mono;

public interface ChatService {
    Mono<ChatResponseDto> recommend(ChatRequestDto request);
}
