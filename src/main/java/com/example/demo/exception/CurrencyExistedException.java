package com.example.demo.exception;

import com.example.demo.exception.model.DomainCode;

public class CurrencyExistedException extends DomainException {

  public CurrencyExistedException() {
    super(DomainCode.CURRENCY_EXSITED);
  }

}
