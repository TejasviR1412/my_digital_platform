package com.digital_platform.digital_wallet.controller;

import com.digital_platform.digital_wallet.exception.DepositLimitExceededException;
import com.digital_platform.digital_wallet.exception.MinDepositAndWithDrawalException;
import com.digital_platform.digital_wallet.exception.WithdrawalLimitExceededException;
import com.digital_platform.digital_wallet.exception.ZeroBalanceException;
import com.digital_platform.digital_wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@SessionAttributes("userId")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount,
                          @ModelAttribute("userId") Long userId,
                          Model model){
        try{
            BigDecimal newBalance = walletService.deposit(userId,amount);
            model.addAttribute("balance",newBalance);
        }catch (DepositLimitExceededException | MinDepositAndWithDrawalException e){
            model.addAttribute("balance",walletService.getBalance(userId));
            model.addAttribute("error",e.getMessage());
        }

        return "balance";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount,
                           @ModelAttribute("userId") Long userId,
                           Model model){
        try{
            BigDecimal newBalance = walletService.withdraw(userId, amount);
            model.addAttribute("balance",newBalance);
        }catch (WithdrawalLimitExceededException | ZeroBalanceException | MinDepositAndWithDrawalException e){
            model.addAttribute("balance",walletService.getBalance(userId));
            model.addAttribute("error",e.getMessage());
        }

        return "balance";
    }

    @GetMapping("/balance")
    public String showBalance(@ModelAttribute("userId") Long userId,
                              Model model){
        model.addAttribute("balance",walletService.getBalance(userId));
        return "balance";
    }
}