package com.projects.currency_converter.controller;

import com.projects.currency_converter.model.ExchangeRate;
import com.projects.currency_converter.model.CurrencyConversionRequest;
import com.projects.currency_converter.service.CurrencyService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/currency")
@RequiredArgsConstructor
@Hidden
public class CurrencyController {
    final private CurrencyService currencyService;

    final private static Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    @GetMapping("/converter")
    public String showConverterForm(Model model){
        model.addAttribute("conversionRequest" , new CurrencyConversionRequest());
        return "currency-converter";
    }


    @PostMapping("/convert")
    public String convertCurrency(@ModelAttribute CurrencyConversionRequest request,
                          Model model){
        try{
            double convertedAmount = currencyService.convert(request.getFromCurrency(),
                                                             request.getToCurrency(),
                                                             request.getAmount());

            request.setConvertedAmount(convertedAmount);
            model.addAttribute("conversionRequest",request);
            model.addAttribute("conversionSuccess",true);
        }catch (Exception e){
            logger.error("CurrencyController :: convert :: Exception occurred!!");
            model.addAttribute("conversionRequest",request);
            model.addAttribute("errorMessage","Could not convert currency: " + e.getMessage());
        }

        return "currency-converter";
    }

    @GetMapping("/rates")
    public String showAllRates(Model model) {
        List<ExchangeRate> rates = currencyService.getAllRates();
        model.addAttribute("exchangeRates", rates);
        return "exchange-rates";
    }

    @GetMapping("/rates/{baseCurrency}")
    public String showRatesForCurrency(@PathVariable String baseCurrency, Model model) {
        List<ExchangeRate> rates = currencyService.getAllRates()
                .stream()
                .filter(rate -> rate.getFromCurrency().equals(baseCurrency.toUpperCase()))
                .collect(Collectors.toList());

        model.addAttribute("exchangeRates", rates);
        model.addAttribute("baseCurrency", baseCurrency.toUpperCase());
        return "exchange-rates";
    }
}