package com.example.so_rich_backend.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一 API 响应格式
 */
@Data
@NoArgsConstructor
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> success() {
        return new Result<T>(200, "success", null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<T>(code, message, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<T>(500, message, null);
    }
}
