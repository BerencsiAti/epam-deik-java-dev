package com.epam.training.ticketservice.core.exceptions;

public class AlreadyExistsException extends Exception {

    public AlreadyExistsException(String what) {
        super(what + " already exists");
    }
}
