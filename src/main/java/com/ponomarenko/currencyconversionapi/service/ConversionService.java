package com.ponomarenko.currencyconversionapi.service;

import com.ponomarenko.currencyconversionapi.entity.ConversionResult;
import com.ponomarenko.currencyconversionapi.exception.ExternalAPIException;
import com.ponomarenko.currencyconversionapi.exception.InvalidInputException;

import java.math.BigDecimal;

public interface ConversionService {

    ConversionResult convert(String from, String to, BigDecimal quantity) throws ExternalAPIException, InvalidInputException;
}
