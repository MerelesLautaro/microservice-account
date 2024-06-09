package com.lautadev.microservice_account.Throwable;

public class AccountException extends RuntimeException{
    public AccountException(String message) {
        super(message);
    }
}
