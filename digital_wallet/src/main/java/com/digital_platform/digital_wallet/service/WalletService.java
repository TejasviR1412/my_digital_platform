package com.digital_platform.digital_wallet.service;

import com.digital_platform.digital_wallet.model.Transaction;
import com.digital_platform.digital_wallet.model.User;
import com.digital_platform.digital_wallet.model.enums.TransactionType;
import com.digital_platform.digital_wallet.repository.TransactionRepository;
import com.digital_platform.digital_wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletService {
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    @Value("${wallet.maxDepositPerDay}")
    private BigDecimal maxDepositPerDay;

    private static final BigDecimal UNIT = BigDecimal.valueOf(100);
    private static final BigDecimal MIN_BALANCE = BigDecimal.valueOf(100);

    public BigDecimal deposit(Long userId , BigDecimal amount){
        validateAmount(amount);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        //calculate total deposits per day
        BigDecimal totalDepositedToday = transactionRepository.sumDepositsByUserAndDate(userId, LocalDate.now());
        if(totalDepositedToday == null)
            totalDepositedToday = BigDecimal.ZERO;
        else if(totalDepositedToday.add(amount).compareTo(maxDepositPerDay) > 0)
            throw new RuntimeException("Deposit limit exceeded for today");

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        saveTransaction(user,TransactionType.DEPOSIT,amount);

        return user.getBalance();
    }

    public BigDecimal withdraw(Long userId, BigDecimal amount){
        validateAmount(amount);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getBalance().subtract(amount).compareTo(MIN_BALANCE) < 0){
            throw new RuntimeException("Balance too low");
        }

        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        saveTransaction(user,TransactionType.WITHDRAWAL,amount);
        return user.getBalance();
    }

    public BigDecimal getBalance(Long userId){
        return userRepository.findById(userId).orElseThrow().getBalance();
    }

    private void validateAmount(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0 ||
        amount.remainder(UNIT).compareTo(BigDecimal.ZERO) !=0){
            throw new RuntimeException("Must be positive & multiple of 100");
        }
    }

    private void saveTransaction(User user , TransactionType type , BigDecimal amount){
        transactionRepository.save(new Transaction(LocalDateTime.now(),amount,type,user));
    }

}