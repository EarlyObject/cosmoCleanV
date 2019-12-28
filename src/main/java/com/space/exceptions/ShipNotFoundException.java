package com.space.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ShipNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3104722110218715223L;

    public ShipNotFoundException(String message) {
        super(message);
    }

}
