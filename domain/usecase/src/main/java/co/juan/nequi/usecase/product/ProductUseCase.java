package co.juan.nequi.usecase.product;

import co.juan.nequi.dto.BranchProductDto;
import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.model.branchproduct.BranchProduct;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import co.juan.nequi.model.exceptions.BranchNotFoundException;
import co.juan.nequi.model.product.Product;
import co.juan.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;
    private final BranchProductRepository branchProductRepository;
    private final BranchRepository branchRepository;

    public Mono<BranchProductDto> saveProduct(BranchProductDto branchProduct) {
        return branchRepository.exitsBranchById(branchProduct.getIdBranch())
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Mono.error(new BranchNotFoundException(branchProduct.getIdBranch()));
                    }

                    Product productToFindOrSave = new Product(null, branchProduct.getName());

                    return productRepository.findProductByName(productToFindOrSave.getName())
                            .switchIfEmpty(productRepository.saveProduct(productToFindOrSave))
                            .flatMap(savedProduct -> {
                                BranchProduct branchProductToSave = new BranchProduct(
                                        null,
                                        branchProduct.getIdBranch(),
                                        savedProduct.getId(),
                                        branchProduct.getStock());

                                return branchProductRepository.saveBranchProduct(branchProductToSave)
                                        .map(savedBranchProduct ->
                                                new BranchProductDto(
                                                        savedProduct.getId(),
                                                        savedProduct.getName(),
                                                        savedBranchProduct.getStock(),
                                                        savedBranchProduct.getIdBranch()
                                                ));
                            });
                });
    }
}
