package com.microservices.exception;

import com.microservices.enums.ErrorCode;
import com.microservices.dto.error.ErrorDetailDTO;

import java.util.List;

/**
 * Excepción lanzada cuando fallan las validaciones de datos
 */
public class ValidationException extends RuntimeException {
    
    private final ErrorCode errorCode;
    private final List<ErrorDetailDTO> errorDetails;
    
    public ValidationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = List.of(ErrorDetailDTO.fromErrorCode(errorCode));
    }
    
    public ValidationException(String message, ErrorCode errorCode, List<ErrorDetailDTO> errorDetails) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public List<ErrorDetailDTO> getErrorDetails() {
        return errorDetails;
    }
}
