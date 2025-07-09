package com.projects.currency_converter.repository;

import com.projects.currency_converter.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,String> {
   Optional<ExchangeRate> findByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);

   @Query("SELECT e FROM ExchangeRate e WHERE e.fromCurrency = :fromCurrency AND e.localDateTime = :date")
   List<ExchangeRate> findByFromCurrencyAndDate(@Param("fromCurrency") String fromCurrency, @Param("date") LocalDate date);

   boolean existsByFromCurrencyAndLocalDateTime(String fromCurrency, LocalDate date);
}