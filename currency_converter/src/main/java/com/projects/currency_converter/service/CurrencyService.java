package com.projects.currency_converter.service;

import com.projects.currency_converter.model.ExchangeRate;
import com.projects.currency_converter.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    final private ExchangeRateRepository exchangeRateRepository;

    final private ExchangeRateUpdaterService exchangeRateUpdaterService;

    public double convert(String from, String to, double amount){
        if(from.equalsIgnoreCase(to))
            return amount;

        Optional<ExchangeRate> existingRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(from,to);

        if(existingRate.isPresent())
            return amount * existingRate.get().getRate().doubleValue();

        exchangeRateUpdaterService.updateRatesForBaseCurrency(from.toUpperCase());

        return exchangeRateRepository.findByFromCurrencyAndToCurrency(from.toUpperCase(),to.toUpperCase())
                .map(rate -> amount * rate.getRate().doubleValue())
                .orElseThrow(() -> new RuntimeException("Rate not found for from currency:" + from + " and to currency:" + to));
    }

    public List<ExchangeRate> getAllRates(){
        return exchangeRateRepository.findAll();
    }
}