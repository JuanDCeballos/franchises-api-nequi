package co.juan.nequi.r2dbc;

import co.juan.nequi.model.branchproduct.BranchProduct;
import co.juan.nequi.r2dbc.entity.BranchProductEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BranchProductReactiveRepository extends ReactiveCrudRepository<BranchProductEntity, Long>,
        ReactiveQueryByExampleExecutor<BranchProductEntity> {

    Mono<BranchProduct> findByIdBranchAndIdProduct(Long idBranch, Long idProduct);
}
