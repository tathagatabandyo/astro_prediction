package com.techtechnicworld.astroPrediction.exception;

import org.springframework.http.HttpStatus;

public class PaymentException extends ApplicationException {
    public PaymentException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    public PaymentException(String message) {
        super(HttpStatus.BAD_REQUEST, "PAYMENT_ERROR", message);
    }
}