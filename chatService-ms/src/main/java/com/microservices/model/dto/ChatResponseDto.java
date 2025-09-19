package com.microservices.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponseDto {
    private String decision;
    private ProductDto productRecommended;
    private String rawOpenAiText;
}
