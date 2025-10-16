package co.juan.nequi.model.franchise.gateways;

import co.juan.nequi.dto.TopStockPerBranchDto;
import co.juan.nequi.model.franchise.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> saveFranchise(Franchise franchise);

    Mono<Boolean> existsFranchiseById(Long idFranchise);

    Mono<Franchise> findFranchiseById(Long idFranchise);

    Flux<TopStockPerBranchDto> findTopStockProductByBranchForFranchise(Long idFranchise);
}
