package com.projects.currency_converter.scheduler;

import com.projects.currency_converter.service.ExchangeRateUpdaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateUpdateScheduler {
    @Autowired
    private ExchangeRateUpdaterService updaterService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateRatesDaily(){
        //some common currencies
        String[] commonBaseCurrencies = {"USD", "EUR", "GBP", "JPY", "INR"};

        for(String baseCurrency : commonBaseCurrencies)
            updaterService.updateRatesForBaseCurrency(baseCurrency);
    }
}