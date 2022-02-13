package com.web.todo.util.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.json.XML;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class XmlConverter implements ApiConverter<Object>{
    private final ObjectMapper objectMapper;

    @Override
    public Object convertToObject(String apiResultStr) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.readValue(XML.toJSONObject(apiResultStr).toString(), Object.class);
        }
        catch (JsonProcessingException jsonProcessingException){
            throw new RuntimeException("Json convert error");
        }
    }
}
