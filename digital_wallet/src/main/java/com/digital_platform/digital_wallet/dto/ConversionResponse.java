package com.digital_platform.digital_wallet.dto;

import java.math.BigDecimal;

public class ConversionResponse {
    private BigDecimal convertedAmount;

    public ConversionResponse() {
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}