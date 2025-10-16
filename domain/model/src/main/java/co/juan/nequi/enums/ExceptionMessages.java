package co.juan.nequi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessages {

    FRANCHISE_NOT_FOUND(404, "Franchise not found."),
    BRANCH_NOT_FOUND(404, "Branch not found."),
    PRODUCT_NOT_FOUND(404, "Product not found."),
    BRANCH_PRODUCT_RELATION(404, "The product does not belong to the branch.");

    private final int code;
    private final String message;
}
