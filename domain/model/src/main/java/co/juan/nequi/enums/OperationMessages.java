package co.juan.nequi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OperationMessages {

    REQUEST_RECEIVED("Request received with params: {}"),
    ENTITY_SAVED_SUCCESSFULLY("Entity saved successfully with params: {}"),
    ENTITY_UPDATED_SUCCESSFULLY("Entity updated successfully with params: {}"),
    ;

    private final String message;
}
