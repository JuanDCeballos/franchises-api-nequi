package co.juan.nequi.usecase.branchproduct;

import co.juan.nequi.model.branchproduct.BranchProduct;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchProductUseCase {

    private final BranchProductRepository branchProductRepository;

    public Mono<BranchProduct> saveBranchProduct(BranchProduct branchProduct) {
        return branchProductRepository.saveBranchProduct(branchProduct);
    }

    public Mono<Void> deleteProductFromBranch(Long idBranch, Long idProduct) {
        return branchProductRepository.findRelationByIdBranchAndIdProduct(idBranch, idProduct)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("The product does not belong to that branch")))
                .flatMap(branchProduct -> branchProductRepository.deleteProductFromBranch(branchProduct.getId()));
    }
}
