package com.control.estoquemanagement.controller.Exception;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiError {
    private String message;
    private HttpStatus status;
    private ZonedDateTime timestamp;
    private String path;

    public ApiError(String message, HttpStatus status, ZonedDateTime timestamp, String path) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
