package co.juan.nequi.usecase.branch;

import co.juan.nequi.model.branch.Branch;
import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.exceptions.BranchNotFoundException;
import co.juan.nequi.exceptions.FranchiseNotFoundException;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> saveBranch(Branch branch) {
        return franchiseRepository.existsFranchiseById(branch.getIdFranchise())
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Mono.error(new FranchiseNotFoundException(branch.getIdFranchise()));
                    }

                    return branchRepository.saveBranch(branch);
                });
    }

    public Mono<Branch> updateBranchName(Long idBranch, String newName) {
        return branchRepository.findBranchById(idBranch)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(idBranch)))
                .flatMap(branchToUpdate -> {
                    branchToUpdate.setName(newName);
                    return branchRepository.saveBranch(branchToUpdate);
                });
    }
}
