package com.digital_platform.digital_wallet.exception;

public class ZeroBalanceException extends RuntimeException{
    public ZeroBalanceException(String message) {
        super(message);
    }
}