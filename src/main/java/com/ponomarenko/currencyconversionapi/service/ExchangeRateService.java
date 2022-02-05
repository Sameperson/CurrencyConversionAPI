package com.ponomarenko.currencyconversionapi.service;

import com.ponomarenko.currencyconversionapi.entity.CurrencyExchangeResponse;
import com.ponomarenko.currencyconversionapi.exception.ExternalAPIException;

public interface ExchangeRateService {

    CurrencyExchangeResponse getExchangeRate(String from, String to) throws ExternalAPIException;
}
