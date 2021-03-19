package com.vandeilson.APIwallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LojistaCanNotTransferMoneyException extends Exception {
    public LojistaCanNotTransferMoneyException() {super("Lojistas are not allowed to send money. Only to receive");}
}
