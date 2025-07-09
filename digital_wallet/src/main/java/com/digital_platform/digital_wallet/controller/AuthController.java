package com.digital_platform.digital_wallet.controller;

import com.digital_platform.digital_wallet.model.User;
import com.digital_platform.digital_wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@SessionAttributes("userId")
public class AuthController {
    private final UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignupForm(Model model){
        model.addAttribute("user",new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user , Model model){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            model.addAttribute("error","Username already exists");
            return "signup";
        }

        user.setBalance(BigDecimal.ZERO);

        User saved = userRepository.save(user);
        model.addAttribute("userId",saved.getId());
        model.addAttribute("balance",saved.getBalance());

        return "balance";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        model.addAttribute("user",new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user,Model model){
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if(existing.isEmpty()){
            model.addAttribute("    error","User not found");
            return "login";
        }

        model.addAttribute("userId",existing.get().getId());
        model.addAttribute("balance",existing.get().getBalance());

        return "balance";
    }

    @PostMapping("/logout")
    public String logout(SessionStatus status){
        status.setComplete();
        return "redirect:/login";
    }
}