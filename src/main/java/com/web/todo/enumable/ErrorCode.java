package com.web.todo.enumable;

public enum ErrorCode {
    SAVE_ERROR("S_001"), INVALID_ERROR("v_001");

    private final String errorCode;

    ErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode;
    }
}
