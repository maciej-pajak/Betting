package pl.maciejpajak.exception;

import javax.persistence.EntityNotFoundException;

public class BaseEntityNotFoundException extends EntityNotFoundException {

    private static final String MESSAGE_PATTERN = "Entity with id %s can not be found";

    public BaseEntityNotFoundException(Long id) {
        super(String.format(MESSAGE_PATTERN, id));

    }

    public BaseEntityNotFoundException(String message) {
        super(message);
    }
    
}
