package com.web.todo.template.api;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class NationalDayRequestTest {

    @Test
    void doRequest() throws Exception{
        //given
        Map mockMap = mock(Map.class);
        NationalDayRequest nationalDayRequest = new NationalDayRequest();

        given(mockMap.get("solYear")).willReturn("2022");
        given(mockMap.get("solMonth")).willReturn("01");

        //when
        HttpURLConnection con = nationalDayRequest.doRequest("http://google.com", mockMap);

        //then
        assertThat(con, is(not(nullValue())));
        assertThat(con.getRequestMethod(), is("GET"));
        assertThat(con.getURL().getHost(), is("google.com"));
        assertThat(con.getRequestProperty("Content-type"), is("application/json"));
    }
}