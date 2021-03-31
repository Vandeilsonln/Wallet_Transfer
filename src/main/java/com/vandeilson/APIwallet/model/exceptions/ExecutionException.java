package com.vandeilson.APIwallet.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExecutionException extends Exception{

    public ExecutionException(String message) {super(message);}
}
