package com.pedrorod.atividade.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}