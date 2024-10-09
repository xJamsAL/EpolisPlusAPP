package com.example.epolisplusapp.models;

public class BaseResponse<T> {
    private boolean code;
    private String message;
    private T response;
    private int status;

    public BaseResponse(boolean code, String message, T response, int status) {
        this.code = code;
        this.message = message;
        this.response = response;
        this.status = status;
    }

    public boolean isCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
