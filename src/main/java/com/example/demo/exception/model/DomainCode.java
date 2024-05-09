package com.example.demo.exception.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum DomainCode {
  UNKNOWN_ERROR("000", "Unknown error"),
  INVALID_INPUT("001", "Invalid input"),
  MISSING_REQUEST_HEADER("002", "Missing request header"),
  INVALID_INPUT_FIELD("003", "Invalid input field %s"),

  CANT_FOUND_CURRENCY("004", "Can't found currency"),
  CURRENCY_EXSITED("005", "Currency existed"),
  INVALID_FORMAT_CURRENCY("006", "Invalid format currency");


  private final String value;

  private final String message;

  private static final Map<String, DomainCode> ENUM_MAP;

  static {
    final Map<String, DomainCode> map = new HashMap<>();
    for (DomainCode instance : DomainCode.values()) {
      map.put(instance.getValue(), instance);
    }
    ENUM_MAP = Collections.unmodifiableMap(map);
  }

  DomainCode(String value, String message) {
    this.value = value;
    this.message = message;
  }

  public String toUniversalCode() {
    return String.format("%s%s%s", SystemIdentifier.CORE_SYSTEM.getCode(),
        ServiceIdentifier.COIN_DESK.getCode(), value);
  }

  public String getMessage() {
    return message;
  }

  public String getValue() {
    return value;
  }

  public static DomainCode get(String value) {
    return ENUM_MAP.get(value);
  }

}
