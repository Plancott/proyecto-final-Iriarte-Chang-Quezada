package com.microservices.dto.error;

import com.microservices.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para detalles de errores en la API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetailDTO {
    
    private String code; // Código único del error
    private String message; // Mensaje del error
    private String field; // Campo específico que causó el error (opcional)
    
    // Crea un ErrorDetailDTO desde un ErrorCode
    public static ErrorDetailDTO fromErrorCode(ErrorCode errorCode) {
        return ErrorDetailDTO.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
    
    // Crea un ErrorDetailDTO desde un ErrorCode con campo específico
    public static ErrorDetailDTO fromErrorCode(ErrorCode errorCode, String field) {
        return ErrorDetailDTO.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .field(field)
                .build();
    }
}
