package pl.maciejpajak.exception;

import javax.persistence.EntityNotFoundException;

public class BetClosedException extends EntityNotFoundException {

    private static final String MESSAGE_PATTERN = "Bet with id %s is already closed and cannot be placed.";

    public BetClosedException(Long id) {
        super(String.format(MESSAGE_PATTERN, id));

    }

    public BetClosedException(String message) {
        super(message);
    }
    
}
