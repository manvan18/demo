package com.example.demo.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.connector.CoinDeskConnector;
import com.example.demo.connector.model.CoinDeskResponse;
import com.example.demo.connector.model.CurrencyInfo;
import com.example.demo.connector.model.Time;
import com.example.demo.exception.CurrencyExistedException;
import com.example.demo.exception.CurrencyNotFoundException;
import com.example.demo.infra.repository.CurrencyRepository;
import com.example.demo.infra.repository.enity.CurrencyEntity;
import com.example.demo.service.Impl.CoinDeskServiceImpl;
import com.example.demo.service.model.Currency;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CoinDeskServiceTest {

  @InjectMocks
  private CoinDeskServiceImpl coinDeskService;

  @Mock
  private CurrencyRepository currencyRepository;

  @Mock
  private CoinDeskConnector coinDeskConnector;

  @Test
  void shouldInitCurrencyPriceSuccessfully() {
    CoinDeskResponse coinDeskResponse = this.createCoinDeskResponse();
    when(coinDeskConnector.GetCurrentPrice()).thenReturn(coinDeskResponse);
    coinDeskService.initCurrencyPrice();
    verify(currencyRepository, times(1))
        .save(any());
  }

  @Test
  void shouldUpdateCurrenciesPriceSuccessfully() {
    CoinDeskResponse coinDeskResponse = this.createCoinDeskResponse();
    when(currencyRepository.findAll()).thenReturn(
        Collections.singletonList(CurrencyEntity.builder().code("USD").build()));
    when(coinDeskConnector.GetCurrentPrice()).thenReturn(coinDeskResponse);
    coinDeskService.scheduleUpdate();
    verify(currencyRepository, times(1))
        .save(any());
  }

  @Test
  void shouldGetAllCurrenciesSuccessfully() {
    when(currencyRepository.findAllByOrderByCodeDesc()).thenReturn(
        Collections.singletonList(CurrencyEntity.builder()
            .code("USD")
            .description("$")
            .symbol("$")
            .rate(BigDecimal.valueOf(4546.77))
            .updatedDate(LocalDateTime.now())
            .createdDate(LocalDateTime.now())
            .build()));
    coinDeskService.getCurrencies();
    verify(currencyRepository, times(1))
        .findAllByOrderByCodeDesc();
  }

  @Test
  void shouldCreateCurrencyPriceSuccessfully() {
    Currency currency = Currency.builder()
        .code("USD")
        .description("$")
        .symbol("$")
        .rate("4546.77")
        .build();
    when(currencyRepository.save(any(CurrencyEntity.class))).thenReturn(CurrencyEntity.builder()
        .code("USD")
        .description("$")
        .symbol("$")
        .rate(BigDecimal.valueOf(4546.77))
        .updatedDate(LocalDateTime.now())
        .createdDate(LocalDateTime.now())
        .build());
    Currency result = coinDeskService.createCurrency(currency);
    assertThat(result.getCode(), is("USD"));
    assertThat(result.getRate(), is("4546.77"));
    verify(currencyRepository, times(1))
        .save(any());
  }

  @Test
  void shouldThrowExisted_whenCreateCurrencyPrice() {
    Currency currency = Currency.builder()
        .code("USD")
        .description("$")
        .symbol("$")
        .rate("4546.77")
        .build();
    when(currencyRepository.findById("USD")).thenReturn(Optional.ofNullable(CurrencyEntity.builder()
        .code("USD")
        .description("$")
        .symbol("$")
        .rate(BigDecimal.valueOf(4546.77))
        .updatedDate(LocalDateTime.now())
        .build()));
    assertThrows(CurrencyExistedException.class,
        () -> coinDeskService.createCurrency(currency));
    verify(currencyRepository, times(0))
        .save(any());
  }

  @Test
  void shouldUpdateCurrencyPriceSuccessfully() {
    Currency currency = Currency.builder()
        .code("USD")
        .description("$")
        .symbol("$")
        .rate("4546.77")
        .build();
    when(currencyRepository.findById("USD")).thenReturn(
        Optional.ofNullable(CurrencyEntity.builder()
            .code("USD")
            .description("$")
            .symbol("$")
            .rate(BigDecimal.valueOf(4546.77))
            .updatedDate(LocalDateTime.now())
            .build()));
    when(currencyRepository.save(any(CurrencyEntity.class))).thenReturn(CurrencyEntity.builder()
        .code("USD")
        .description("$")
        .symbol("$")
        .rate(BigDecimal.valueOf(4546.77))
        .updatedDate(LocalDateTime.now())
        .build());
    Currency result = coinDeskService.updateCurrency(currency);
    assertThat(result.getCode(), is("USD"));
    assertThat(result.getRate(), is("4546.77"));
    verify(currencyRepository, times(1))
        .save(any());
  }

  @Test
  void shouldThrowNotFound_whenUpdateCurrencyPrice() {
    Currency currency = Currency.builder()
        .code("USD")
        .description("$")
        .symbol("$")
        .rate("4546.77")
        .build();
    assertThrows(CurrencyNotFoundException.class,
        () -> coinDeskService.updateCurrency(currency));
    verify(currencyRepository, times(0))
        .save(any());
  }


  @Test
  void shouldDeleteCurrencyPriceSuccessfully() {
    when(currencyRepository.findById("USD")).thenReturn(
        Optional.ofNullable(CurrencyEntity.builder().build()));
    doNothing().when(currencyRepository).deleteById("USD");
    coinDeskService.deleteCurrency("USD");
    verify(currencyRepository, times(1))
        .deleteById("USD");
  }


  @Test
  void shouldThrowNotFound_whenDeleteCurrencyPrice() {
    assertThrows(CurrencyNotFoundException.class,
        () -> coinDeskService.deleteCurrency("USD"));
    verify(currencyRepository, times(0))
        .deleteById(any());
  }

  private CoinDeskResponse createCoinDeskResponse() {
    CurrencyInfo currencyInfo = CurrencyInfo.builder()
        .code("USD")
        .description("$")
        .symbol("$")
        .rate(BigDecimal.valueOf(4546.77))
        .build();
    Time time = Time.builder().updatedISO(LocalDateTime.now()).build();
    return CoinDeskResponse.builder()
        .time(time)
        .bpi(Map.of("USD", currencyInfo))
        .build();
  }

}
