package com.digital_platform.digital_wallet.service;

import com.digital_platform.digital_wallet.model.User;
import com.digital_platform.digital_wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User signup(String username,
                       String password){
        if(userRepository.findByUsername(username).isPresent())
            throw new RuntimeException("User already exists");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return userRepository.save(user);
    }

    public User login(String username,
                      String password){
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Invalid login."));
    }
}