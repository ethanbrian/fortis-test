package com.example.accounts.exception;

public class InternalServerErrorException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public InternalServerErrorException() {
        super();
    }

    private String message = "Internal Server Error";


    public InternalServerErrorException(final String message, final Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public InternalServerErrorException(String message) {
        super(message);
        this.message = message;
    }


    public InternalServerErrorException(final Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
