package com.ponomarenko.currencyconversionapi.controller;

import com.ponomarenko.currencyconversionapi.entity.ConversionResult;
import com.ponomarenko.currencyconversionapi.exception.ExternalAPIException;
import com.ponomarenko.currencyconversionapi.exception.InvalidInputException;
import com.ponomarenko.currencyconversionapi.service.ConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyConversionController {

    private final ConversionService conversionService;

    @Autowired
    public CurrencyConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Operation(summary = "Convert from one currency to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully converted the currency",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/convert")
    public ConversionResult calculateCurrencyConversion(@RequestParam @Parameter(description = "Currency you would like to convert from") String from,
                                                        @RequestParam String to,
                                                        @RequestParam BigDecimal quantity)
            throws ExternalAPIException, InvalidInputException {

        return conversionService.convert(from, to, quantity);
    }
}
