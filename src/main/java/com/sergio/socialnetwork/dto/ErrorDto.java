package com.sergio.socialnetwork.dto;

public class ErrorDto {

    private String message;

    public ErrorDto() {
        super();
    }

    public ErrorDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
