package co.juan.nequi.r2dbc;

import co.juan.nequi.r2dbc.entity.BranchProductEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BranchProductReactiveRepository extends ReactiveCrudRepository<BranchProductEntity, Long>,
        ReactiveQueryByExampleExecutor<BranchProductEntity> {

}
