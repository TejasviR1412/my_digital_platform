package com.projects.currency_converter.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Currency Converion request object")
public class CurrencyConversionRequest {

    @Schema(description = "Source currency code" , example = "USD")
    private String fromCurrency;

    @Schema(description = "Target currency code" , example = "EUR")
    private String toCurrency;

    @Schema(description = "Amount to convert" , example = "100.50")
    private double amount;

    @Schema(description = "Converted Amount result" , example = "85.25" , accessMode = Schema.AccessMode.READ_ONLY)
    private double convertedAmount;
}