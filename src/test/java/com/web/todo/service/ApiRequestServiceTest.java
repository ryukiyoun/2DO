package com.web.todo.service;

import com.web.todo.template.api.RequestApiTemplate;
import com.web.todo.util.api.ApiConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ApiRequestServiceTest {
    @Mock
    RequestApiTemplate requestApiTemplate;

    @Mock
    ApiConverter<Object> xmlConverter;

    @InjectMocks
    ApiRequestService apiRequestService;

    @Test
    void nationalDayRequest() {
        //given
        given(xmlConverter.convertToObject(anyString())).willReturn("{\"id\":1}");
        given(requestApiTemplate.requestApi(anyString(), anyMap())).willReturn("test api request success");

        //when, then
        assertThat(apiRequestService.nationalDayRequest("test", new HashMap<>()), is("{\"id\":1}"));
    }
}