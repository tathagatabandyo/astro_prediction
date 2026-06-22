package com.techtechnicworld.astroPrediction.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*
     * =========================
     * Custom Application Exceptions
     * =========================
     */

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(
            ApplicationException ex) {

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(
                        ex.getMessage(),
                        ex.getCode()));
    }

    /*
     * =========================
     * Validation Exceptions
     * =========================
     */

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleHandlerMethodValidation(
            HandlerMethodValidationException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getParameterValidationResults().forEach(result -> {

            String field = result.getMethodParameter().getParameterName();

            if (field == null || field.isBlank()) {
                field = "field";
            }

            String finalField = field;

            result.getResolvableErrors().forEach(error -> errors.put(
                    finalField,
                    error.getDefaultMessage()));
        });

        return build(
                HttpStatus.BAD_REQUEST,
                "Validation failed.",
                errors,
                "VALIDATION_FAILED");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleBodyValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a));

        return build(
                HttpStatus.BAD_REQUEST,
                "Validation failed.",
                errors,
                "VALIDATION_FAILED");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {

            errors.put(
                    violation.getPropertyPath().toString(),
                    violation.getMessage());
        }

        return build(
                HttpStatus.BAD_REQUEST,
                "Validation failed.",
                errors,
                "VALIDATION_FAILED");
    }

    /*
     * =========================
     * Multipart/File Upload Errors
     * =========================
     */

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingPart(
            MissingServletRequestPartException ex) {

        return build(
                HttpStatus.BAD_REQUEST,
                ex.getRequestPartName() + " is required.",
                null,
                "MISSING_REQUEST_PART");
    }

    /*
     * =========================
     * Request Parameter Errors
     * =========================
     */

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingParameter(
            MissingServletRequestParameterException ex) {

        return build(
                HttpStatus.BAD_REQUEST,
                "Required request parameter is missing: "
                        + ex.getParameterName(),
                null,
                "MISSING_PARAMETER");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return build(
                HttpStatus.BAD_REQUEST,
                "Invalid value for parameter: "
                        + ex.getName(),
                null,
                "INVALID_PARAMETER");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingHeader(
            MissingRequestHeaderException ex) {

        return build(
                HttpStatus.BAD_REQUEST,
                "Required header is missing: "
                        + ex.getHeaderName(),
                null,
                "MISSING_HEADER");
    }

    /*
     * =========================
     * JSON / Request Body Errors
     * =========================
     */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleUnreadableBody(
            HttpMessageNotReadableException ex) {

        return build(
                HttpStatus.BAD_REQUEST,
                "Invalid request body or malformed JSON.",
                null,
                "INVALID_REQUEST_BODY");
    }

    /*
     * =========================
     * HTTP Method Errors
     * =========================
     */

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex) {

        String supported = ex.getSupportedHttpMethods() == null
                ? ""
                : ex.getSupportedHttpMethods()
                        .stream()
                        .map(method -> method.name())
                        .collect(Collectors.joining(", "));

        return build(
                HttpStatus.METHOD_NOT_ALLOWED,
                "HTTP method not supported. Allowed: "
                        + supported,
                null,
                "METHOD_NOT_ALLOWED");
    }

    /*
     * =========================
     * 404 Errors
     * =========================
     */

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandler(
            NoHandlerFoundException ex) {

        return build(
                HttpStatus.NOT_FOUND,
                "No API found for "
                        + ex.getHttpMethod()
                        + " "
                        + ex.getRequestURL(),
                null,
                "NOT_FOUND");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoResource(
            NoResourceFoundException ex) {

        return build(
                HttpStatus.NOT_FOUND,
                "API endpoint not found.",
                null,
                "NOT_FOUND");
    }

    /*
     * =========================
     * Common Java Exceptions
     * =========================
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return build(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null,
                "BAD_REQUEST");
    }

    /*
     * =========================
     * Fallback Exception
     * =========================
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(
            Exception ex) {

        log.error("Unhandled exception", ex);

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong.",
                null,
                "INTERNAL_SERVER_ERROR");
    }

    /*
     * =========================
     * Common Response Builder
     * =========================
     */

    private ResponseEntity<ApiResponse<?>> build(
            HttpStatus status,
            String message,
            Object data,
            String code) {

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(
                        message,
                        code,
                        data));
    }
}