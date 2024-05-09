package com.example.demo.controller;

import com.example.demo.controller.dto.CurrencyRequest;
import com.example.demo.service.CoinDeskService;
import com.example.demo.service.model.Currency;
import jakarta.validation.Valid;
import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("coindesk")
@Validated
public class CoinDeskController {

  @Autowired
  private CoinDeskService coinDeskService;

  @GetMapping(value = "/v1/currencies")
  @ResponseStatus(HttpStatus.OK)
  public List<Currency> getCurrencies() {
    return coinDeskService.getCurrencies();
  }

  @PostMapping(value = "/v1/currencies")
  @ResponseStatus(HttpStatus.CREATED)
  public Currency createCurrency(@Valid @RequestBody CurrencyRequest currencyRequest)
      throws ParseException {
    return coinDeskService.createCurrency(Currency.builder()
        .code(currencyRequest.getCode())
        .symbol(currencyRequest.getSymbol())
        .rate(currencyRequest.getRate().stripTrailingZeros().toPlainString())
        .description(currencyRequest.getDescription())
        .build());
  }

  @DeleteMapping(value = "/v1/currencies/{code}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void deleteCurrency(@PathVariable("code") String code) {
    coinDeskService.deleteCurrency(code);
  }

  @PutMapping(value = "/v1/currencies")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Currency updateCurrency(@RequestBody @Valid CurrencyRequest currencyRequest)
      throws ParseException {
    return coinDeskService.updateCurrency(Currency.builder()
        .code(currencyRequest.getCode())
        .symbol(currencyRequest.getSymbol())
        .rate(currencyRequest.getRate().stripTrailingZeros().toPlainString())
        .description(currencyRequest.getDescription())
        .build());
  }

}
