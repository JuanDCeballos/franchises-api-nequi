package co.juan.nequi.r2dbc;

import co.juan.nequi.model.franchise.Franchise;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
import co.juan.nequi.r2dbc.entity.FranchiseEntity;
import co.juan.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Franchise,
        FranchiseEntity,
        Long,
        FranchiseReactiveRepository
        > implements FranchiseRepository {
    public FranchiseReactiveRepositoryAdapter(FranchiseReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return save(franchise);
    }
}
