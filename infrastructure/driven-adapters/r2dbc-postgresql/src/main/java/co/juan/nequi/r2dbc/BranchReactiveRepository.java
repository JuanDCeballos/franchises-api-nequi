package co.juan.nequi.r2dbc;

import co.juan.nequi.r2dbc.entity.BranchEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BranchReactiveRepository extends ReactiveCrudRepository<BranchEntity, Long>,
        ReactiveQueryByExampleExecutor<BranchEntity> {

}
