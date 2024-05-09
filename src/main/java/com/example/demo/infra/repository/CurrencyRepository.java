package com.example.demo.infra.repository;

import com.example.demo.infra.repository.enity.CurrencyEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {

  List<CurrencyEntity> findAllByOrderByCodeDesc();

}
