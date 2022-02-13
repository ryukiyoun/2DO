package com.web.todo.util.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class XmlConverterTest {
    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    XmlConverter xmlConverter;

    @Test
    void convertToObject() throws Exception{
        //given
        given(objectMapper.enable(ArgumentMatchers.any(SerializationFeature.class))).willReturn(objectMapper);
        given(objectMapper.readValue(anyString(), any(Class.class))).willReturn("test");

        //when
        Object result = xmlConverter.convertToObject(anyString());

        //then
        assertThat(result, is("test"));
    }

    @Test
    void convertToObjectException() throws Exception{
        //given
        given(objectMapper.enable(ArgumentMatchers.any(SerializationFeature.class))).willReturn(objectMapper);
        given(objectMapper.readValue(anyString(), any(Class.class))).willThrow(new JsonProcessingException("") {});

        //when then
        assertThrows(RuntimeException.class, () -> xmlConverter.convertToObject(anyString()));
    }
}