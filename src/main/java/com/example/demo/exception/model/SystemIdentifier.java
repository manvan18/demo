package com.example.demo.exception.model;

public enum SystemIdentifier {
  CORE_SYSTEM("01");

  private String code;

  SystemIdentifier(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
