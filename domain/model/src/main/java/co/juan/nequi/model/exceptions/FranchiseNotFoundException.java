package co.juan.nequi.model.exceptions;

public class FranchiseNotFoundException extends RuntimeException {
    public FranchiseNotFoundException(Long idFranchise) {
        super("Franchise with id: " + idFranchise + " not found.");
    }
}
