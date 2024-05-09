package com.example.demo.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class CurrencyRequest implements Serializable {

  @NotNull
  private String code;
  @NotNull
  private String symbol;
  @NotNull
  private String description;
  @NotNull
  @DecimalMin(value = "0", inclusive = false, message = "Rate must be greater than 0")
  private BigDecimal rate;
}
