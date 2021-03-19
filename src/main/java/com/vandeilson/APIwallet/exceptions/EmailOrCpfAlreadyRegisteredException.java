package com.vandeilson.APIwallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailOrCpfAlreadyRegisteredException extends Exception {
    public EmailOrCpfAlreadyRegisteredException() {super(String.format("Either the email or the CPF is already registered"));}
}
