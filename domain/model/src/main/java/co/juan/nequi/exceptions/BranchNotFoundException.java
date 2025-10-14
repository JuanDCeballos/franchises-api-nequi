package co.juan.nequi.exceptions;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(Long idBranch) {
        super("Branch with id: " + idBranch + " not found.");
    }
}
