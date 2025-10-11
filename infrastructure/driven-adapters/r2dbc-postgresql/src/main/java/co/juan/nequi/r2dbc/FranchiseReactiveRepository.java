package co.juan.nequi.r2dbc;

import co.juan.nequi.r2dbc.entity.FranchiseEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranchiseReactiveRepository extends ReactiveCrudRepository<FranchiseEntity, Long>,
        ReactiveQueryByExampleExecutor<FranchiseEntity> {

    Mono<Boolean> existsById(Long idFranchise);
}
