package co.juan.nequi.usecase.franchise;

import co.juan.nequi.dto.TopStockPerBranchDto;
import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import co.juan.nequi.exceptions.FranchiseNotFoundException;
import co.juan.nequi.model.franchise.Franchise;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
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
        return Flux.empty();
    }

    public Mono<Franchise> updateFranchiseName(Long idFranchise, String newName) {
        return franchiseRepository.findFranchiseById(idFranchise)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(idFranchise)))
                .flatMap(franchiseToUpdate -> {
                    franchiseToUpdate.setName(newName);
                    return franchiseRepository.saveFranchise(franchiseToUpdate);
                });
    }
}
