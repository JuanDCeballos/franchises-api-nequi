package co.juan.nequi.exceptions;

public class BranchProductRelationException extends RuntimeException {
    public BranchProductRelationException(Long idProduct, Long idBranch) {
        super("Product with id: " + idProduct + " does not belong to branch with id: " + idBranch);
    }
}
