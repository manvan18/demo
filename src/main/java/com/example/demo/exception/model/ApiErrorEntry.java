package com.example.demo.exception.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class ApiErrorEntry {

  private String errorCode;

  private String errorMessage;

  private Object metadata;

}
