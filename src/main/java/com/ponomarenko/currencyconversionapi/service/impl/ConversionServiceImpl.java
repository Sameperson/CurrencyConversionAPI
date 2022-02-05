package com.ponomarenko.currencyconversionapi.service.impl;

import com.ponomarenko.currencyconversionapi.annotation.TrackExecutionTime;
import com.ponomarenko.currencyconversionapi.entity.ConversionResult;
import com.ponomarenko.currencyconversionapi.entity.CurrencyExchangeResponse;
import com.ponomarenko.currencyconversionapi.exception.ExternalAPIException;
import com.ponomarenko.currencyconversionapi.exception.InvalidInputException;
import com.ponomarenko.currencyconversionapi.service.ConversionService;
import com.ponomarenko.currencyconversionapi.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConversionServiceImpl implements ConversionService {

    private static final Set<String> VALID_CURRENCIES = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet());

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ConversionServiceImpl(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @TrackExecutionTime
    @Override
    public ConversionResult convert(String from, String to, BigDecimal quantity)
            throws ExternalAPIException, InvalidInputException {
        validateCurrencyCode(from);
        validateCurrencyCode(to);

        log.info("Getting exchange rate from the external API...");
        CurrencyExchangeResponse exchangeRate = exchangeRateService.getExchangeRate(from, to);

        BigDecimal conversionMultiplier = extractConversionMultiplier(exchangeRate);

        BigDecimal result = quantity.multiply(conversionMultiplier);

        return new ConversionResult(from, to, quantity, conversionMultiplier, result);
    }

    private void validateCurrencyCode(String currencyCode) throws InvalidInputException {
        if (!VALID_CURRENCIES.contains(currencyCode.toUpperCase())) {
            throw new InvalidInputException("Invalid currency code: " + currencyCode +
                    ". Make sure that currency code is in ISO 4217 format");
        }
    }

    private BigDecimal extractConversionMultiplier(CurrencyExchangeResponse exchangeRate)
            throws ExternalAPIException {

        if (exchangeRate.getRates().isEmpty()) {
            throw new ExternalAPIException("External API didn't return conversion result");
        }

        return exchangeRate.getRates()
                .entrySet()
                .iterator()
                .next()
                .getValue();
    }
}
