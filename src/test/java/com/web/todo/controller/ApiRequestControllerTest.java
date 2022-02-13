package com.web.todo.controller;

import com.web.todo.service.ApiRequestService;
import com.web.todo.service.UserFindService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ApiRequestController.class, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@WithMockUser(username = "user")
class ApiRequestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserFindService userFindService;

    @MockBean
    ApiRequestService apiRequestService;

    @MockBean
    AuthenticationSuccessHandler customLoginSuccessHandler;

    @Test
    void getNationalDay() throws Exception{
        //given
        given(apiRequestService.nationalDayRequest(anyString(), anyMap())).willReturn("test api request success");

        //when, then
        mockMvc.perform(get("/api/national/day")
                .param("apiURL", "test")
                .param("key", "testKey")
                .param("solYear", "2020")
                .param("solMonth", "01"))
                .andExpect(status().isOk())
                .andExpect(content().string("test api request success"))
                .andDo(print());
    }
}
