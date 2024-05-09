package com.example.demo.service.model;


import static com.example.demo.utils.CommonUtils.parseString;

import com.example.demo.config.LocalDateTimeSerializer;
import com.example.demo.infra.repository.enity.CurrencyEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Currency implements Serializable {

  private String code;
  private String symbol;
  private String rate;
  private String description;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime updatedDate;

  public CurrencyEntity toCurrencyEntity() {
    return CurrencyEntity.builder()
        .code(this.getCode())
        .symbol(this.getSymbol())
        .description(this.getDescription())
        .rate(parseString(this.getRate()))
        .build();
  }

}
