package com.example.android.intouch_android.model.container;

public class ApiException extends Exception {
    private ApiExceptionType exceptionType;
    public ApiException(ApiExceptionType exceptionType, String errorMessage) {
        super(errorMessage);
        this.exceptionType = exceptionType;
    }

    public ApiExceptionType getExceptionType() { return  this.exceptionType; }
}