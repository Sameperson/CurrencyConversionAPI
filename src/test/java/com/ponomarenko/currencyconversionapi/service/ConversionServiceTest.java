package com.ponomarenko.currencyconversionapi.service;

import com.ponomarenko.currencyconversionapi.entity.ConversionResult;
import com.ponomarenko.currencyconversionapi.entity.CurrencyExchangeResponse;
import com.ponomarenko.currencyconversionapi.exception.ExternalAPIException;
import com.ponomarenko.currencyconversionapi.exception.InvalidInputException;
import com.ponomarenko.currencyconversionapi.service.impl.ConversionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class ConversionServiceTest {
    public static final double MOCKED_RATE = 4.2;
    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ConversionServiceImpl conversionService;

    private CurrencyExchangeResponse currencyExchangeResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        currencyExchangeResponse = new CurrencyExchangeResponse();

        Map<String, BigDecimal> rates = Collections.singletonMap("UAH", BigDecimal.valueOf(MOCKED_RATE));
        currencyExchangeResponse.setRates(rates);
        currencyExchangeResponse.setSuccess(true);
        currencyExchangeResponse.setBase("EUR");

    }

    @Test
    public void testConversionSuccessful() throws InvalidInputException, ExternalAPIException {
        Mockito.when(exchangeRateService.getExchangeRate(anyString(), anyString())).thenReturn(currencyExchangeResponse);

        double inputAmount = 601.13;
        ConversionResult convertResult = conversionService.convert("EUR", "UAH", BigDecimal.valueOf(inputAmount));

        assertNotNull(convertResult);
        assertEquals(convertResult.getTotalCalculatedAmount(), BigDecimal.valueOf(inputAmount * MOCKED_RATE));
    }

    @Test
    public void testInvalidToCurrencyThrowsException() throws ExternalAPIException {
        Mockito.when(exchangeRateService.getExchangeRate(anyString(), anyString())).thenReturn(currencyExchangeResponse);

        String invalidCurrencyCode = "WWW";
        assertThrows(InvalidInputException.class, () -> conversionService.convert("EUR", invalidCurrencyCode, BigDecimal.valueOf(12.12)));
    }

    @Test
    public void testInvalidFromCurrencyThrowsException() throws ExternalAPIException {
        Mockito.when(exchangeRateService.getExchangeRate(anyString(), anyString())).thenReturn(currencyExchangeResponse);

        String invalidCurrencyCode = "ZZZ";
        assertThrows(InvalidInputException.class, () -> conversionService.convert(invalidCurrencyCode, "UAH", BigDecimal.valueOf(12.12)));
    }


    @Test
    public void testConversionExceptionFromExternalRestCall() throws ExternalAPIException {
        Mockito.when(exchangeRateService.getExchangeRate(anyString(), anyString())).thenThrow(ExternalAPIException.class);

        assertThrows(ExternalAPIException.class, () -> conversionService.convert("EUR", "UAH", BigDecimal.valueOf(12.12)));
    }
}
