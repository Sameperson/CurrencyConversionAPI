package com.ponomarenko.currencyconversionapi.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class CurrencyExchangeResponse {
    private boolean isSuccess;
    private String base;
    private Map<String, BigDecimal> rates;
}
