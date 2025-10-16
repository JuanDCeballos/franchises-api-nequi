package co.juan.nequi.usecase.branchproduct;

import co.juan.nequi.enums.ExceptionMessages;
import co.juan.nequi.exceptions.BusinessException;
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
                .switchIfEmpty(Mono.error(new BusinessException(ExceptionMessages.BRANCH_PRODUCT_RELATION)))
                .flatMap(branchProduct -> branchProductRepository.deleteProductFromBranch(branchProduct.getId()));
    }

    public Mono<BranchProduct> updateProductStock(Long idBranch, Long idProduct, Long newStock) {
        return branchProductRepository.findRelationByIdBranchAndIdProduct(idBranch, idProduct)
                .switchIfEmpty(Mono.error(new BusinessException(ExceptionMessages.BRANCH_PRODUCT_RELATION)))
                .flatMap(branchProduct -> {
                    branchProduct.setStock(newStock);
                    return branchProductRepository.saveBranchProduct(branchProduct);
                });
    }
}
