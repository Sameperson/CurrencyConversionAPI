package com.ponomarenko.currencyconversionapi.service.impl;

import com.ponomarenko.currencyconversionapi.entity.CurrencyExchangeError;
import com.ponomarenko.currencyconversionapi.entity.CurrencyExchangeResponse;
import com.ponomarenko.currencyconversionapi.exception.ExternalAPIException;
import com.ponomarenko.currencyconversionapi.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class ExchangeRatesAPIService implements ExchangeRateService {

    public static final Duration TIMEOUT = Duration.ofSeconds(30);
    public static final String BASE_CURRENCY_FREE_SUBSCRIPTION = "EUR";
    private final WebClient exchangeApiWebClient;

    @Value("${exchangerates.api.key}")
    private String apiKey;
    @Value("${exchangerates.api.free-subscription}")
    private boolean isFreeSubscription;

    @Autowired
    public ExchangeRatesAPIService(WebClient exchangeApiWebClient) {
        this.exchangeApiWebClient = exchangeApiWebClient;
    }

    @Override
    public CurrencyExchangeResponse getExchangeRate(String from, String to) throws ExternalAPIException {
        validateBaseCurrencyForSubscription(from);

        return exchangeApiWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", from)
                        .queryParam("symbols", to)
                        .queryParam("access_key", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::isError, this::processError)
                .bodyToMono(CurrencyExchangeResponse.class)
                .block(TIMEOUT);
    }

    private void validateBaseCurrencyForSubscription(String from) throws ExternalAPIException {
        if (isFreeSubscription && !from.equalsIgnoreCase(BASE_CURRENCY_FREE_SUBSCRIPTION)) {
            throw new ExternalAPIException("Free subscription of the Exchangerates API only supports EUR as a base currency");
        }
    }

    private Mono<Throwable> processError(ClientResponse response) {
        return response.bodyToMono(CurrencyExchangeError.class)
                .flatMap(e -> Mono.error(new ExternalAPIException("Message from external API: " + e.getMessage())));
    }
}
