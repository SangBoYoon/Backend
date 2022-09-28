package com.sangboyoon.accounter.domain.security.exceptions;

// 이미 가입 된 계정일 때
public class CEmailSignupFailedException extends RuntimeException{
    public CEmailSignupFailedException() {
        super();
    }

    public CEmailSignupFailedException(String message) {
        super(message);
    }

    public CEmailSignupFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}