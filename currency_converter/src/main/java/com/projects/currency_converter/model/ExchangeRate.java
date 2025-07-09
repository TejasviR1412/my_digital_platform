package com.projects.currency_converter.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name="exchange_rate")
@Schema(description = "Exchange rate entity")
public class ExchangeRate {
    @Id
    @Schema(description = "Currency Pair Identifier" , example = "USD_EUR")
    private String currencyPair;

    @Schema(description = "Source currency code" , example = "USD")
    private String fromCurrency;

    @Schema(description = "Target currency code" , example = "EUR")
    private String toCurrency;

    @Schema(description = "Exchange rate value" , example = "0.85")
    private BigDecimal rate;

    @Schema(description = "Last update date" , example = "2025-07-09")
    private LocalDate localDateTime;
}