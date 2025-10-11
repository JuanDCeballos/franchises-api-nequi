package co.juan.nequi.usecase.branch;

import co.juan.nequi.model.branch.Branch;
import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.model.exceptions.FranchiseNotFoundException;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchUseCaseTest {

    @InjectMocks
    BranchUseCase branchUseCase;

    @Mock
    BranchRepository branchRepository;

    @Mock
    FranchiseRepository franchiseRepository;

    private Branch branch;

    @BeforeEach
    void initMocks() {
        branch = new Branch();
        branch.setId(1L);
        branch.setName("MiBanco - Centro");
        branch.setIdFranchise(1L);
    }

    @Test
    void saveBranch() {
        when(franchiseRepository.existsFranchiseById(anyLong())).thenReturn(Mono.just(true));
        when(branchRepository.saveBranch(any(Branch.class))).thenReturn(Mono.just(branch));

        Mono<Branch> response = branchUseCase.saveBranch(branch);

        StepVerifier.create(response)
                .expectNextMatches(val -> val.equals(branch))
                .verifyComplete();

        verify(franchiseRepository, times(1)).existsFranchiseById(anyLong());
        verify(branchRepository, times(1)).saveBranch(any(Branch.class));
    }

    @Test
    void saveBranchReturnExceptionWhenFranchiseNotFound() {
        when(franchiseRepository.existsFranchiseById(anyLong())).thenReturn(Mono.just(false));

        Mono<Branch> response = branchUseCase.saveBranch(branch);

        StepVerifier.create(response)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepository, times(1)).existsFranchiseById(anyLong());
        verify(branchRepository, times(0)).saveBranch(any(Branch.class));
    }
}
