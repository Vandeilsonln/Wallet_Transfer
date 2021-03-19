package com.vandeilson.APIwallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PayerDoesNotHaveEnoughMoney extends Exception {
    public PayerDoesNotHaveEnoughMoney(Float payerCurrentAmount, Float valueToBeDeducted){
        super(String.format("you do not have enough funds for this transacations.\nYour funds: %s\tValue to be deducted: %s", payerCurrentAmount, valueToBeDeducted));
    }

}
