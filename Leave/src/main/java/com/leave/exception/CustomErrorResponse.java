package com.leave.exception;

import java.time.LocalDateTime;

public class CustomErrorResponse {
    private String status;
    private LocalDateTime timeStamp;
    private String statusMessage;
    private int statusCode;

    public CustomErrorResponse() {
        // Default constructor
    }

    public CustomErrorResponse(String status, String statusMessage) {
        this.status = status;
        this.timeStamp = LocalDateTime.now();
        this.statusMessage = statusMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

