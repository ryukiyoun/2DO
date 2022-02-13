package com.web.todo.template.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestApiTemplateTest {

    @Test
    void doRequestOK() throws Exception{
        //given
        HttpURLConnection conn = mock(HttpURLConnection.class);
        RequestApiTemplate template = mock(RequestApiTemplate.class, Mockito.CALLS_REAL_METHODS);

        given(conn.getResponseCode()).willReturn(200);
        given(conn.getInputStream()).willReturn(new ByteArrayInputStream("test api request success".getBytes()));

        //when
        when(template.doRequest(anyString(), anyMap())).thenReturn(conn);

        //then
        assertThat(template.requestApi("test", new HashMap<>()), is("test api request success"));
    }

    @Test
    void doRequestBadRequest() throws Exception{
        //given
        HttpURLConnection conn = mock(HttpURLConnection.class);
        RequestApiTemplate template = mock(RequestApiTemplate.class, Mockito.CALLS_REAL_METHODS);

        given(conn.getResponseCode()).willReturn(400);
        given(conn.getErrorStream()).willReturn(new ByteArrayInputStream("test api request error".getBytes()));

        //when
        when(template.doRequest(anyString(), anyMap())).thenReturn(conn);

        //then
        assertThat(template.requestApi("test", new HashMap<>()), is("test api request error"));
    }

    @Test
    void doRequestProcessing() throws Exception{
        //given
        HttpURLConnection conn = mock(HttpURLConnection.class);
        RequestApiTemplate template = mock(RequestApiTemplate.class, Mockito.CALLS_REAL_METHODS);

        given(conn.getResponseCode()).willReturn(100);
        given(conn.getErrorStream()).willReturn(new ByteArrayInputStream("test api request process".getBytes()));

        //when
        when(template.doRequest(anyString(), anyMap())).thenReturn(conn);

        //then
        assertThat(template.requestApi("test", new HashMap<>()), is("test api request process"));
    }

    @Test
    void doRequestException() throws Exception{
        //given
        RequestApiTemplate template = mock(RequestApiTemplate.class, Mockito.CALLS_REAL_METHODS);

        //when
        when(template.doRequest(anyString(), anyMap())).thenThrow(new IOException());

        //then
        assertThrows(RuntimeException.class, () -> template.requestApi("testURL", new HashMap<>()));
    }

    @Test
    void bufReaderCloseException() throws Exception{
        //given
        InputStream connInputStream = mock(InputStream.class);
        HttpURLConnection conn = mock(HttpURLConnection.class);
        BufferedReader bufferedReader = mock(BufferedReader.class);
        RequestApiTemplate template = mock(RequestApiTemplate.class, Mockito.CALLS_REAL_METHODS);

        given(conn.getResponseCode()).willReturn(200);
        given(conn.getInputStream()).willReturn(connInputStream);

        //when
        when(template.doRequest(anyString(), anyMap())).thenReturn(conn);
        when(template.makeBufReader(connInputStream)).thenReturn(bufferedReader);
        when(bufferedReader.readLine()).thenReturn("test api request process").thenReturn(null);
        doThrow(new IOException()).when(bufferedReader).close();

        //then
        assertThat(template.requestApi("test", new HashMap<>()), is("test api request process"));
    }
}