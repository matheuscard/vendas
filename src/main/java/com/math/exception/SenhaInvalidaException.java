package com.math.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super("Senha inválida");
    }
}
