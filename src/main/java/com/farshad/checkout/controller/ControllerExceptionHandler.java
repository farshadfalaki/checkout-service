package com.farshad.checkout.controller;

import com.farshad.checkout.exception.CheckoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CheckoutException.class})
    public String checkoutException(CheckoutException e){
      log.warn("CheckoutException {}",e.toString());
      return e.getMessage();
    }

}
