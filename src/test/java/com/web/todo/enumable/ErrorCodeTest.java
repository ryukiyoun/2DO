package com.web.todo.enumable;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ErrorCodeTest {
    @Test
    public void TodoStateGetValueTest(){
        ErrorCode saveError = ErrorCode.SAVE_ERROR;
        ErrorCode invalidError = ErrorCode.INVALID_ERROR;

        assertThat(saveError.getCode(), is(ErrorCode.SAVE_ERROR.getCode()));
        assertThat(invalidError.getCode(), is(ErrorCode.INVALID_ERROR.getCode()));
    }
}
