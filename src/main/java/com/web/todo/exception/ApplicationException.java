package com.web.todo.exception;

import com.web.todo.enumable.ErrorCode;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{
    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode, String message, Throwable exception){
        super(message, exception);
        this.errorCode = errorCode;
    }
}
