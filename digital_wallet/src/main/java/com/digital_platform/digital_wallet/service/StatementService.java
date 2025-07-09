package com.digital_platform.digital_wallet.service;

import com.digital_platform.digital_wallet.model.Transaction;
import com.digital_platform.digital_wallet.model.User;
import com.digital_platform.digital_wallet.repository.TransactionRepository;
import com.digital_platform.digital_wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Value("${wallet.maxTransactions:20}")
    private int maxTransactions;

    public List<Transaction> getMiniStatement(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.findTo20ByUserOrderByDateDesc(user);
    }
}