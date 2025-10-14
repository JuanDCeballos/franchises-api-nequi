package co.juan.nequi.model.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long idProduct) {
        super("Product with id: " + idProduct + " not found.");
    }
}
