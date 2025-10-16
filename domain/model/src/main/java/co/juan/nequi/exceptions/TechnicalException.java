package co.juan.nequi.exceptions;

import co.juan.nequi.enums.ExceptionMessages;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final ExceptionMessages exceptionMessages;

    public TechnicalException(ExceptionMessages message) {
        super(message.getMessage());
        this.exceptionMessages = message;
    }
}
