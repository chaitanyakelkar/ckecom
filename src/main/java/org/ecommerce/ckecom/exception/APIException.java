package org.ecommerce.ckecom.exception;

public class APIException extends RuntimeException {
    private final static Long SerialVersionUID = 1L;

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }
}
