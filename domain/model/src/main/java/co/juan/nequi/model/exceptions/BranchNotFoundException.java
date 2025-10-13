package co.juan.nequi.model.exceptions;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(Long idBranch) {
        super("Branch with id: " + idBranch + " not found.");
    }
}
