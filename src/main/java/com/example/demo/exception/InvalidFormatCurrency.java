package com.example.demo.exception;

import com.example.demo.exception.model.DomainCode;

public class InvalidFormatCurrency extends DomainException {

  public InvalidFormatCurrency(String key) {
    super(DomainCode.INVALID_FORMAT_CURRENCY, new Object[]{key});
  }

}