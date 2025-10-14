package co.juan.nequi.r2dbc;

import co.juan.nequi.model.branch.Branch;
import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.r2dbc.entity.BranchEntity;
import co.juan.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BranchReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Branch,
        BranchEntity,
        Long,
        BranchReactiveRepository
        > implements BranchRepository {
    public BranchReactiveRepositoryAdapter(BranchReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Branch.class));
    }

    @Override
    public Mono<Branch> saveBranch(Branch branch) {
        return save(branch);
    }

    @Override
    public Mono<Boolean> exitsBranchById(Long idBranch) {
        return repository.existsById(idBranch);
    }

    @Override
    public Flux<Branch> findBranchByIdFranchise(Long idFranchise) {
        return repository.findByIdFranchise(idFranchise);
    }
}
