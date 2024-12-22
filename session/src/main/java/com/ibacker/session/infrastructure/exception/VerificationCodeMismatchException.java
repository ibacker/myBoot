package com.ibacker.session.infrastructure.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VerificationCodeMismatchException extends RuntimeException{

    private int code;
    private String message;

    public VerificationCodeMismatchException(String message) {
        this.code = 400;
        this.message = message;
    }

    public VerificationCodeMismatchException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
