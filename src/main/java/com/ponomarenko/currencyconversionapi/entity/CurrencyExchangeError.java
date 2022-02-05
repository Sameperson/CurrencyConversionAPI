package com.ponomarenko.currencyconversionapi.entity;

import lombok.Data;
import lombok.experimental.Delegate;

@Data
public class CurrencyExchangeError {
    @Delegate
    private ErrorMessageBody error;

    @Data
    private static class ErrorMessageBody {
        private String message;
        private String details;
    }
}
