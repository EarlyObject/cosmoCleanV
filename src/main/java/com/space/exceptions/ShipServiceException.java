package com.space.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ShipServiceException extends RuntimeException {

    private static final long serialVersionUID = 350599510629702550L;

    public ShipServiceException(String message) {
        super(message);
    }
}
