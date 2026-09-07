package com.rookies6.myspringbootlab.exception;

import java.time.LocalDateTime;

public class ErrorObject {

    private Integer statusCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorObject(Integer statusCode, String message, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}