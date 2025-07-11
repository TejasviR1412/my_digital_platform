package com.digital_platform.digital_wallet.service;

import com.digital_platform.digital_wallet.exception.DepositLimitExceededException;
import com.digital_platform.digital_wallet.exception.MinDepositAndWithDrawalException;
import com.digital_platform.digital_wallet.exception.WithdrawalLimitExceededException;
import com.digital_platform.digital_wallet.exception.ZeroBalanceException;
import com.digital_platform.digital_wallet.model.Transaction;
import com.digital_platform.digital_wallet.model.User;
import com.digital_platform.digital_wallet.model.enums.TransactionType;
import com.digital_platform.digital_wallet.repository.TransactionRepository;
import com.digital_platform.digital_wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${wallet.maxDepositPerDay}")
    private BigDecimal maxDepositPerDay;

    @Value("${wallet.maxWithdrawalPerDay}")
    private BigDecimal maxWithdrawalPerDay;

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
            throw new DepositLimitExceededException("Deposit limit exceeded.You cannot deposit more than 10,000 in a day.");

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        saveTransaction(user,TransactionType.DEPOSIT,amount);

        return user.getBalance();
    }

    public BigDecimal withdraw(Long userId, BigDecimal amount){
        validateAmount(amount);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        //calculate total withdrawals per day
        BigDecimal totalWithdrawalsToday = transactionRepository.sumWithdrawalsByUserAndDate(userId,LocalDate.now());

        if(totalWithdrawalsToday == null)
            totalWithdrawalsToday = BigDecimal.ZERO;
        else if(totalWithdrawalsToday.add(amount).compareTo(maxWithdrawalPerDay)>0)
            throw new WithdrawalLimitExceededException("Withdrawal limit exceeded.You cannot withdraw more than 10,000 in a day");

        if(user.getBalance().subtract(amount).compareTo(MIN_BALANCE) < 0){
            throw new ZeroBalanceException("You cannot have zero balance.");
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
            throw new MinDepositAndWithDrawalException("Must be positive & multiple of 100");
        }
    }

    private void saveTransaction(User user , TransactionType type , BigDecimal amount){
        transactionRepository.save(new Transaction(LocalDateTime.now(),amount,type,user));
    }
}