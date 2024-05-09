package com.example.demo.exception;

import com.example.demo.exception.model.DomainCode;

public class CurrencyNotFoundException extends DomainException {

  public CurrencyNotFoundException() {
    super(DomainCode.CANT_FOUND_CURRENCY);
  }

}

