package co.juan.nequi.usecase.franchise;

import co.juan.nequi.dto.TopStockPerBranchDto;
import co.juan.nequi.model.branch.Branch;
import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.model.branchproduct.BranchProduct;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import co.juan.nequi.model.exceptions.FranchiseNotFoundException;
import co.juan.nequi.model.exceptions.ProductNotFoundException;
import co.juan.nequi.model.franchise.Franchise;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
import co.juan.nequi.model.product.Product;
import co.juan.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final BranchProductRepository branchProductRepository;
    private final ProductRepository productRepository;

    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return franchiseRepository.saveFranchise(franchise);
    }

    public Flux<TopStockPerBranchDto> findTopStockProductByBranch(Long idFranchise) {
        return franchiseRepository.existsFranchiseById(idFranchise)
                .flatMapMany(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Flux.error(new FranchiseNotFoundException(idFranchise));
                    }

                    return branchRepository.findBranchByIdFranchise(idFranchise);
                }).flatMap(branch ->
                        Mono.zip(
                                Mono.just(branch),
                                branchProductRepository.findTopStockByIdBranch(branch.getId()))
                ).flatMap(tuple -> {
                    Branch branch = tuple.getT1();
                    BranchProduct topBranchProduct = tuple.getT2();

                    Mono<Product> productMono = productRepository.findProductById(topBranchProduct.getIdProduct())
                            .switchIfEmpty(Mono.error(new ProductNotFoundException(topBranchProduct.getIdProduct())));

                    return Mono.zip(
                            Mono.just(branch),
                            Mono.just(topBranchProduct),
                            productMono);
                }).map(tuple -> {
                    Branch branch = tuple.getT1();
                    BranchProduct topBranchProduct = tuple.getT2();
                    Product product = tuple.getT3();

                    return TopStockPerBranchDto.builder()
                            .idBranch(branch.getId())
                            .branchName(branch.getName())
                            .idProduct(product.getId())
                            .productName(product.getName())
                            .stock(topBranchProduct.getStock())
                            .build();
                });
    }
}
