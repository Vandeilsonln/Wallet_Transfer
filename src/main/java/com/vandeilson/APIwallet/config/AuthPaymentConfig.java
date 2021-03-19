package com.vandeilson.APIwallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AuthPaymentConfig {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    };
}
