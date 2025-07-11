package com.digital_platform.digital_wallet.controller;

import com.digital_platform.digital_wallet.model.User;
import com.digital_platform.digital_wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        logger.info("Raw password: {}" ,rawPassword);
        logger.info("Encoded password: {}" ,encodedPassword);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

        logger.info("User entered password: {}" ,user.getPassword());
        logger.info("Stored hashed password: {}" ,existing.get().getPassword());

        if(!passwordEncoder.matches(user.getPassword(),existing.get().getPassword())){
            model.addAttribute("error","Invalid password");
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