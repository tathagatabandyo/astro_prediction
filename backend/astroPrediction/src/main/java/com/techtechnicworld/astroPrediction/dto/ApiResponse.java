package com.techtechnicworld.astroPrediction.dto;

public record ApiResponse<T>(boolean error, String message, T data, String code) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(false, message, data, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(false, "Success", data, null);
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(true, message, null, code);
    }

    public static <T> ApiResponse<T> error(String message, String code, T data) {
        return new ApiResponse<>(true, message, data, code);
    }
}
