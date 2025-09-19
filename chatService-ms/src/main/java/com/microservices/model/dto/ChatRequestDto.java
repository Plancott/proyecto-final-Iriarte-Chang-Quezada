package com.microservices.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ChatRequestDto {
    @NotNull
    private Long idUser;

    @NotBlank
    private String menssage;

    @NotNull
    @Size(min = 1)
    private List<ProductDto> products;
}
