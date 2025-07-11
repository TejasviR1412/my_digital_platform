package com.digital_platform.digital_wallet.exception;

public class DepositLimitExceededException extends RuntimeException{
    public DepositLimitExceededException(String message) {
        super(message);
    }
}