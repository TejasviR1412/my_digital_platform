package com.digital_platform.digital_wallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Controller
//@RequiredArgsConstructor
@SessionAttributes("userId")
public class CurrencyController {
    private final RestTemplate restTemplate;

    public CurrencyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        System.out.println("RestTemplate injected: " + restTemplate);
    }

    @Value("${currency.converter.url}")
    private String converterUrl;

    @GetMapping("/convert")
    public String convert(@RequestParam(defaultValue = "INR") String from,
                          @RequestParam(defaultValue = "USD") String to,
                          @RequestParam(defaultValue = "100")BigDecimal amount,
                          Model model){
        try {
            String url = String.format("%s?from=%s&to=%s&amount=%s",converterUrl,from,to,amount);
            BigDecimal converetedAmount = restTemplate.getForObject(url, BigDecimal.class);
            model.addAttribute("originalAmount" , amount);
            model.addAttribute("from",from);
            model.addAttribute("to",to);
            model.addAttribute("converetedAmount",converetedAmount);
        }catch (Exception e){
            model.addAttribute("error","Failed to convert:" + e.getMessage());
        }

        return "converter";
    }
}