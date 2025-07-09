package com.projects.currency_converter.service;

import com.projects.currency_converter.model.ExchangeRate;
import com.projects.currency_converter.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {
    @Mock
    private ExchangeRateRepository repository;

    @Mock
    private ExchangeRateUpdaterService exchangeRateUpdaterService;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    public void test_convert_same_currency(){
        double amount = currencyService.convert("USD","USD",100);
        assertEquals(100, amount);
    }

    @Test
    public void test_with_existing_rate(){
        ExchangeRate exchangeRate = new ExchangeRate();

        exchangeRate.setRate(BigDecimal.valueOf(0.92));

        when(repository.findByFromCurrencyAndToCurrency("USD","EUR")).thenReturn(Optional.of(exchangeRate));

        double converted = currencyService.convert("USD","EUR",100);

        assertEquals(92.0 , converted , 0.01);
    }

    @Test
    public void test_convert_when_rate_not_found_calls_updater_and_succeeds(){
        ExchangeRate rate = new ExchangeRate();
        rate.setRate(BigDecimal.valueOf(0.95));

        when(repository.findByFromCurrencyAndToCurrency("USD","INR"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(rate));

        double converted = currencyService.convert("USD","INR",100);

        assertEquals(95.0,converted,0.01);
        verify(exchangeRateUpdaterService,times(1)).updateRatesForBaseCurrency("USD");
    }

    @Test
    public void test_convert_rate_not_found_even_after_update(){
        when(repository.findByFromCurrencyAndToCurrency("USD","JPY"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class , () -> currencyService.convert("USD","JPY",100));

        verify(exchangeRateUpdaterService,times(1)).updateRatesForBaseCurrency("USD");
    }
}