package com.techtechnicworld.astroPrediction.exception;

import org.springframework.http.HttpStatus;

public class AIProcessingException extends ApplicationException {

    public AIProcessingException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    public AIProcessingException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "AI_ERROR", message);
    }
}
