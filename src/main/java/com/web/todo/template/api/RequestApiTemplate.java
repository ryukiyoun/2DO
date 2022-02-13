package com.web.todo.template.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public abstract class RequestApiTemplate {
    public String requestApi(String apiURL, Map<String, String> param){
        BufferedReader rd = null;
        HttpURLConnection conn = null;
        try {
            conn = doRequest(apiURL, param);
            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 300) {
                rd = makeBufReader(conn.getInputStream());
            } else {
                rd = makeBufReader(conn.getErrorStream());
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        }
        catch (IOException ioException){
            throw new RuntimeException("api request error");
        }
        finally {
            if(rd != null) {
                try {
                    rd.close();
                } catch (IOException ignore) {

                }
            }

            if(conn != null)
                conn.disconnect();
        }
    }

    public BufferedReader makeBufReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    protected abstract HttpURLConnection doRequest(String apiURL, Map<String, String> param) throws IOException;
}
