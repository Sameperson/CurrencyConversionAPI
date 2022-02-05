package com.ponomarenko.currencyconversionapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExternalAPIException extends Exception {
    public ExternalAPIException(String message) {
        super(message);
    }
}
