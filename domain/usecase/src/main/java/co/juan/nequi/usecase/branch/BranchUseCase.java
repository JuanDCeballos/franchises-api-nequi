package co.juan.nequi.usecase.branch;

import co.juan.nequi.enums.ExceptionMessages;
import co.juan.nequi.exceptions.BusinessException;
import co.juan.nequi.model.branch.Branch;
import co.juan.nequi.model.branch.gateways.BranchRepository;
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
                        return Mono.error(new BusinessException(ExceptionMessages.FRANCHISE_NOT_FOUND));
                    }

                    return branchRepository.saveBranch(branch);
                });
    }

    public Mono<Branch> updateBranchName(Long idBranch, String newName) {
        return branchRepository.findBranchById(idBranch)
                .switchIfEmpty(Mono.error(new BusinessException(ExceptionMessages.BRANCH_NOT_FOUND)))
                .flatMap(branchToUpdate -> {
                    branchToUpdate.setName(newName);
                    return branchRepository.saveBranch(branchToUpdate);
                });
    }
}
