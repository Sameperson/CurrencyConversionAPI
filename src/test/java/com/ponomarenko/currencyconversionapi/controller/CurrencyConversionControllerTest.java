package com.ponomarenko.currencyconversionapi.controller;

import com.ponomarenko.currencyconversionapi.entity.ConversionResult;
import com.ponomarenko.currencyconversionapi.exception.ExternalAPIException;
import com.ponomarenko.currencyconversionapi.exception.InvalidInputException;
import com.ponomarenko.currencyconversionapi.service.ConversionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class CurrencyConversionControllerTest {

    public static final String FROM_CURRENCY = "EUR";
    public static final String TO_CURRENCY = "UAH";
    public static final String QUANTITY = "2.01";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ConversionService conversionService;

    private ConversionResult conversionResult;

    @Before
    public void setUp() {
        conversionResult = new ConversionResult();
        conversionResult.setTotalCalculatedAmount(BigDecimal.valueOf(20.1));
    }

    @Test
    public void testGetConversionStatusOk() throws Exception {
        Mockito.when(conversionService.convert(anyString(), anyString(), any(BigDecimal.class))).thenReturn(conversionResult);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/convert?from=" + FROM_CURRENCY + "&to=" + TO_CURRENCY + "&quantity=" + QUANTITY)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCalculatedAmount").value(conversionResult.getTotalCalculatedAmount()));
    }

    @Test
    public void testGetConversionInvalidInputFromException() throws Exception {
        String invalidInputFrom = "ZZZ";

        Mockito.when(conversionService.convert(anyString(), anyString(), any(BigDecimal.class))).thenThrow(InvalidInputException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/convert?from=" + invalidInputFrom + "&to=" + TO_CURRENCY + "&quantity=" + QUANTITY)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetConversionInvalidInputToException() throws Exception {
        String invalidInputTo = "XXX";

        Mockito.when(conversionService.convert(anyString(), anyString(), any(BigDecimal.class))).thenThrow(InvalidInputException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/convert?from=" + FROM_CURRENCY + "&to=" + invalidInputTo + "&quantity=" + QUANTITY)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetConversionExternalApiError() throws Exception {
        Mockito.when(conversionService.convert(anyString(), anyString(), any(BigDecimal.class))).thenThrow(ExternalAPIException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/convert?from=" + FROM_CURRENCY + "&to=" + TO_CURRENCY + "&quantity=" + QUANTITY)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
