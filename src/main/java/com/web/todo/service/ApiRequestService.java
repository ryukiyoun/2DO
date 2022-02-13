package com.web.todo.service;

import com.web.todo.template.api.RequestApiTemplate;
import com.web.todo.util.api.ApiConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiRequestService {
    private final RequestApiTemplate nationalDayRequest;
    private final ApiConverter<Object> xmlConverter;

    public Object nationalDayRequest(String apiURL, Map<String, String> param) {
        String xmlStr = nationalDayRequest.requestApi(apiURL, param);
        return xmlConverter.convertToObject(xmlStr);
    }
}
