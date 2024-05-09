package com.example.demo.infra.repository.enity;


import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import org.springframework.util.ObjectUtils;

public class CurrencyEntityListener {

  @PrePersist
  public void prePersist(CurrencyEntity entity) {
    LocalDateTime now = LocalDateTime.now();
    if (ObjectUtils.isEmpty(entity.getCreatedDate())) {
      entity.setCreatedDate(now);
    }
    if (ObjectUtils.isEmpty(entity.getUpdatedDate())) {
      entity.setUpdatedDate(now);
    }
  }

  @PreUpdate
  public void preUpdate(CurrencyEntity entity) {
    if (ObjectUtils.isEmpty(entity.getUpdatedDate())) {
      entity.setUpdatedDate(LocalDateTime.now());
    }
  }
}