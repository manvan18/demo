package com.example.demo.service.Impl;

import static com.example.demo.utils.CommonUtils.parseString;

import com.example.demo.connector.CoinDeskConnector;
import com.example.demo.connector.model.CoinDeskResponse;
import com.example.demo.connector.model.CurrencyInfo;
import com.example.demo.exception.CurrencyExistedException;
import com.example.demo.exception.CurrencyNotFoundException;
import com.example.demo.infra.repository.CurrencyRepository;
import com.example.demo.infra.repository.enity.CurrencyEntity;
import com.example.demo.service.CoinDeskService;
import com.example.demo.service.model.Currency;
import jakarta.annotation.PostConstruct;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoinDeskServiceImpl implements CoinDeskService {

  @Autowired
  private CoinDeskConnector coinDeskConnector;

  @Autowired
  private CurrencyRepository currencyRepository;

  @PostConstruct
  public void initCurrencyPrice() {
    CoinDeskResponse coinDeskResponse = coinDeskConnector.GetCurrentPrice();
    LocalDateTime updatedCurrencyDate = coinDeskResponse.getTime().getUpdatedISO();
    for (CurrencyInfo currencyInfo : coinDeskResponse.getBpi().values()) {
      CurrencyEntity currencyEntity = CurrencyEntity.builder()
          .code(currencyInfo.getCode())
          .symbol(currencyInfo.getSymbol())
          .description(currencyInfo.getDescription())
          .rate(currencyInfo.getRate())
          .updatedDate(updatedCurrencyDate)
          .build();
      currencyRepository.save(currencyEntity);
    }
  }

  @Scheduled(fixedRate = 60000)
  public void scheduleUpdate() {
    log.info("Schedule update currency");
    CoinDeskResponse coinDeskResponse = coinDeskConnector.GetCurrentPrice();
    LocalDateTime updatedCurrencyDate = coinDeskResponse.getTime().getUpdatedISO();
    Map<String, CurrencyEntity> currencyMap = currencyRepository.findAll().stream()
        .collect(Collectors.toMap(CurrencyEntity::getCode, Function.identity()));
    for (CurrencyInfo currencyInfo : coinDeskResponse.getBpi().values()) {
      if(currencyMap.containsKey(currencyInfo.getCode())){
      CurrencyEntity entity = currencyMap.get(currencyInfo.getCode());
      entity.setCode(currencyInfo.getCode());
      entity.setSymbol(currencyInfo.getSymbol());
      entity.setDescription(currencyInfo.getDescription());
      entity.setRate(currencyInfo.getRate());
      entity.setUpdatedDate(updatedCurrencyDate);
      currencyRepository.save(entity);
      }
    }
  }

  @Override
  public List<Currency> getCurrencies() {
    return currencyRepository.findAllByOrderByCodeDesc().stream().map(
            e -> Currency.builder().code(e.getCode()).rate(e.getRate().stripTrailingZeros().toPlainString()).description(e.getDescription())
                .symbol(e.getSymbol()).updatedDate(e.getUpdatedDate()).build())
        .collect(Collectors.toList());
  }

  @Override
  public Currency createCurrency(Currency currency)  {
    if(!currencyRepository.findById(currency.getCode()).isEmpty()){
      throw new  CurrencyExistedException();
    }
    CurrencyEntity currencyEntity = currencyRepository.save(currency.toCurrencyEntity());
    return Currency.builder()
        .code(currencyEntity.getCode())
        .symbol(currencyEntity.getSymbol())
        .description(currencyEntity.getDescription())
        .updatedDate(currencyEntity.getUpdatedDate())
        .rate(currencyEntity.getRate().stripTrailingZeros().toPlainString())
        .build();
  }

  @Override
  public void deleteCurrency(String code) {
    currencyRepository.findById(code).orElseThrow(CurrencyNotFoundException::new);
    currencyRepository.deleteById(code);
  }

  @Override
  public Currency updateCurrency(Currency currency)  {
    currencyRepository.findById(currency.getCode()).orElseThrow(CurrencyNotFoundException::new);
    CurrencyEntity currencyEntity = currencyRepository.findById(currency.getCode()).orElseThrow(CurrencyNotFoundException::new);
    String stringValue = currency.getRate().replaceAll("[^0-9.]", "");
    // Parse the cleaned string to BigDecimal
    NumberFormat format = NumberFormat.getInstance(Locale.US);
    currencyEntity.setCode(currency.getCode());
    currencyEntity.setSymbol(currency.getSymbol());
    currencyEntity.setDescription(currency.getDescription());
    currencyEntity.setRate((parseString(currency.getRate())));
    currencyEntity.setUpdatedDate(currency.getUpdatedDate());
    currencyEntity = currencyRepository.save(currencyEntity);
    return Currency.builder()
        .code(currencyEntity.getCode())
        .symbol(currencyEntity.getSymbol())
        .description(currencyEntity.getDescription())
        .updatedDate(currencyEntity.getUpdatedDate())
        .rate(currencyEntity.getRate().stripTrailingZeros().toPlainString())
        .build();
  }


}
