package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.controller.dto.CurrencyRequest;
import com.example.demo.exception.CurrencyNotFoundException;
import com.example.demo.infra.repository.enity.CurrencyEntity;
import com.example.demo.service.CoinDeskService;
import com.example.demo.service.model.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CoinDeskController.class)
public class CoinDeskControllerTest {


  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CoinDeskService coinDeskService;

  @Test
  void shouldGetCurrenciesSuccessfully() throws Exception {
    when(coinDeskService.getCurrencies()).thenReturn(List.of());
    // Then
    mockMvc.perform(get("/coindesk/v1/currencies")
            .contentType("application/json"))
        .andExpect(status().isOk());

    verify(coinDeskService, times(1))
        .getCurrencies();
  }

  @Test
  void shouldCreatedCurrencySuccessfully() throws Exception {
    CurrencyRequest currencyRequest = CurrencyRequest.builder()
        .code("USD")
        .description("USD")
        .symbol("$")
        .rate(BigDecimal.valueOf(3445.555))
        .build();
    when(coinDeskService.createCurrency(any())).thenReturn(Currency.builder().build());
    // Then
    mockMvc.perform(post("/coindesk/v1/currencies")
            .content(objectMapper.writeValueAsString(currencyRequest))
            .contentType("application/json"))
        .andExpect(status().isCreated());

    verify(coinDeskService, times(1))
        .createCurrency(any());
  }

  @Test
  void shouldInvalidRequest_whenCreateCurrency() throws Exception {
    CurrencyRequest currencyRequest = CurrencyRequest.builder()
        .description("USD")
        .symbol("$")
        .rate(BigDecimal.valueOf(3445.555))
        .build();
    when(coinDeskService.createCurrency(any())).thenReturn(Currency.builder().build());
    // Then
    mockMvc.perform(post("/coindesk/v1/currencies")
            .content(objectMapper.writeValueAsString(currencyRequest))
            .contentType("application/json"))
        .andExpect(status().isBadRequest());

    verify(coinDeskService, times(0))
        .createCurrency(any());
  }

  @Test
  void shouldDeleteCurrencySuccessfully() throws Exception {
    doNothing().when(coinDeskService).deleteCurrency("USD");
    // Then
    mockMvc.perform(delete("/coindesk/v1/currencies/USD")
            .contentType("application/json"))
        .andExpect(status().isAccepted());

    verify(coinDeskService, times(1))
        .deleteCurrency("USD");
  }

}
