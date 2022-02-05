package com.ponomarenko.currencyconversionapi.advice;

import com.ponomarenko.currencyconversionapi.entity.ErrorMessage;
import com.ponomarenko.currencyconversionapi.exception.ExternalAPIException;
import com.ponomarenko.currencyconversionapi.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseBody
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ExternalAPIException.class)
    protected ResponseEntity<ErrorMessage> handleExternalApiException(ExternalAPIException ex, WebRequest request) {
        ErrorMessage errorMessageDTO = new ErrorMessage("External API exception", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessageDTO);
    }

    @ExceptionHandler(value = InvalidInputException.class)
    protected ResponseEntity<ErrorMessage> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        ErrorMessage errorMessageDTO = new ErrorMessage("Received invalid input", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessageDTO);
    }
}
