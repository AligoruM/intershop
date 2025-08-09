package com.practice.intershop.exception;

public class PaymentFailedException extends IntershopCustomException{
    public PaymentFailedException(String message) {
        super(message);
    }
}
