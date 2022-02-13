package com.web.todo.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomLoginSuccessHandlerTest {
    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpServletResponse httpServletResponse;

    @Mock
    Authentication authentication;

    AuthenticationSuccessHandler customLoginSuccessHandler = new CustomLoginSuccessHandler();

    @Test
    void onAuthenticationSuccess() throws Exception{
        customLoginSuccessHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);

        verify(httpServletResponse, times(1)).sendRedirect(anyString());
    }
}