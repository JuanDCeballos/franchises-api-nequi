package co.juan.nequi.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long idProduct) {
        super("Product with id: " + idProduct + " not found.");
    }
}
