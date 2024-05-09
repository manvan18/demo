package com.example.demo.infra.repository.enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency")
@EntityListeners(CurrencyEntityListener.class) // Add entity listener
public class CurrencyEntity {

  @Id
  private String code;
  @Column
  private String symbol;
  @Column(precision = 20, scale = 6)
  private BigDecimal rate;
  @Column
  private String description;
  @Column(name = "created_date")
  private LocalDateTime createdDate;
  @Column(name = "updated_date")
  private LocalDateTime updatedDate;
  @Column
  @Version
  private Long version;

}
