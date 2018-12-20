package com.integrationapi.config;

import org.apache.http.client.config.RequestConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfig {

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom().build();
    }

}