package co.juan.nequi.model.branch.gateways;

import co.juan.nequi.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> saveBranch(Branch branch);

    Mono<Boolean> exitsBranchById(Long idBranch);

    Flux<Branch> findBranchByIdFranchise(Long idFranchise);

    Mono<Branch> findBranchById(Long idBranch);
}
