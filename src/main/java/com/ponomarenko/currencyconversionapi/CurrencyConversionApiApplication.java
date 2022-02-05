package com.ponomarenko.currencyconversionapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@OpenAPIDefinition
@SpringBootApplication
public class CurrencyConversionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConversionApiApplication.class, args);
    }

    @Bean
    public WebClient exchangeApiWebClient() {
        return WebClient.create("http://api.exchangeratesapi.io/v1/");
    }

}
