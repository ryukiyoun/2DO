package com.web.todo.template.api;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class NationalDayRequest extends RequestApiTemplate {
    @Override
    protected HttpURLConnection doRequest(String apiURL, Map<String, String> param) throws IOException {
        String urlBuilder = apiURL + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=RkYOAfxfWkUaUsU5IPY7oH%2B%2B8fy1YlfXVaOjzYw8b8xh4g67YL3tg5ZaipTJI65Rs1s%2FsVgBiRlS%2F7R9%2FQKrHw%3D%3D" +
                "&" + URLEncoder.encode("solYear", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(param.get("solYear"), StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("solMonth", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(param.get("solMonth"), StandardCharsets.UTF_8);
        URL url = new URL(urlBuilder);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        return conn;
    }
}
