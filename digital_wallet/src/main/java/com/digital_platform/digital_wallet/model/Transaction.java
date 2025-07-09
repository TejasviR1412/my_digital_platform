package com.digital_platform.digital_wallet.model;

import com.digital_platform.digital_wallet.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    private User user;

    public Transaction(LocalDateTime date, BigDecimal amount, TransactionType type, User user) {
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.user = user;
    }
}