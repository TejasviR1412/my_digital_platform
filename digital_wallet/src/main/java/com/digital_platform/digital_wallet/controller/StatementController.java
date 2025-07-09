package com.digital_platform.digital_wallet.controller;

import com.digital_platform.digital_wallet.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequiredArgsConstructor
@SessionAttributes("userId")
public class StatementController {
    private StatementService statementService;

    @GetMapping("/statement")
    public String statement(@ModelAttribute("userId") Long userId,
                            Model model){
        model.addAttribute("transactions",statementService.getMiniStatement(userId));
        return "statement";
    }
}