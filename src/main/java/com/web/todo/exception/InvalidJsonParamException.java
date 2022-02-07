package com.web.todo.exception;

import com.web.todo.enumable.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidJsonParamException extends ApplicationException{
    private final String json;

    public InvalidJsonParamException(String json, ErrorCode errorCode, String message, Throwable exception) {
        super(errorCode, message, exception);
        this.json = json;
    }
}
