package com.digital_platform.digital_wallet.exception;

public class WithdrawalLimitExceededException extends RuntimeException{
    public WithdrawalLimitExceededException(String message) {
        super(message);
    }
}