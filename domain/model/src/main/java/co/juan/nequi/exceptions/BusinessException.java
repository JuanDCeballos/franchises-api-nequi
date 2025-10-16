package co.juan.nequi.exceptions;

import co.juan.nequi.enums.ExceptionMessages;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ExceptionMessages exceptionMessages;

    public BusinessException(ExceptionMessages message) {
        super(message.getMessage());
        this.exceptionMessages = message;
    }
}
