package co.juan.nequi.usecase.franchise;

import co.juan.nequi.model.franchise.Franchise;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return franchiseRepository.saveFranchise(franchise);
    }
}
