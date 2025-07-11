package com.digital_platform.digital_wallet.repository;

import com.digital_platform.digital_wallet.model.Transaction;
import com.digital_platform.digital_wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findTop20ByUserOrderByDateDesc(User user);

    //Sum of deposits for DEPOSIT for a user on a given date
    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.user.id=:userId AND t.type='DEPOSIT' AND FUNCTION('DATE',t.date) = :date")
    BigDecimal sumDepositsByUserAndDate(@Param("userId") Long userId, @Param("date")LocalDate date);

    //Sum of withdrawals for WITHDRAW for a user on a given date
    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.user.id=:userId AND t.type='WITHDRAW' AND FUNCTION('DATE',t.date) = :date")
    BigDecimal sumWithdrawalsByUserAndDate(@Param("userId") Long userId, @Param("date")LocalDate date);
}