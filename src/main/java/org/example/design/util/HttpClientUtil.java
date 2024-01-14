package org.example.design.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HttpClientUtil {
    @Bean
    public String predict() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("http://localhost:8080/predict")
                .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .header("Accept", "*/*")
                .header("Host", "localhost:8080")
                .header("Connection", "keep-alive")
                .asString();
        return response.getBody();
    }
}
