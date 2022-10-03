package com.sangboyoon.accounter.domain.corporation.exception;

public class CCorporationNotFoundException extends RuntimeException{
    public CCorporationNotFoundException(String message, Throwable cause) { super(message, cause); }

    public CCorporationNotFoundException(String message) { super(message); }

    public CCorporationNotFoundException() { super(); }
}
