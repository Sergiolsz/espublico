package com.consum.orders.domain.exception;

public class InvalidException extends RuntimeException {

    public static class InvalidDateFormatException extends ProcessingException {
        public InvalidDateFormatException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidStringValueException extends ProcessingException {
        public InvalidStringValueException(String message) {
            super(message);
        }
    }

    public static class InvalidNumberValueException extends ProcessingException {
        public InvalidNumberValueException(String message) {
            super(message);
        }
    }

}
