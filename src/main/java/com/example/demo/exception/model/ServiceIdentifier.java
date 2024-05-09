package com.example.demo.exception.model;

public enum ServiceIdentifier {
  COIN_DESK("01");

  private String code;

  ServiceIdentifier(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
