package co.juan.nequi.usecase.franchise;

import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import co.juan.nequi.exceptions.FranchiseNotFoundException;
import co.juan.nequi.model.franchise.Franchise;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
import co.juan.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @InjectMocks
    FranchiseUseCase franchiseUseCase;

    @Mock
    FranchiseRepository franchiseRepository;

    @Mock
    BranchRepository branchRepository;

    @Mock
    BranchProductRepository branchProductRepository;

    @Mock
    ProductRepository productRepository;

    private final Long idFranchise = 1L;
    private final String newName = "Mi Banco";

    private Franchise franchise;

    @BeforeEach
    void initMocks() {
        franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("MiBanco");
    }

    @Test
    void saveFranchise() {
        when(franchiseRepository.saveFranchise(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<Franchise> response = franchiseUseCase.saveFranchise(franchise);

        StepVerifier.create(response)
                .expectNextMatches(val -> val.equals(franchise))
                .verifyComplete();

        verify(franchiseRepository, times(1)).saveFranchise(any(Franchise.class));
    }

    @Test
    void updateFranchiseName() {
        when(franchiseRepository.findFranchiseById(anyLong())).thenReturn(Mono.just(franchise));
        when(franchiseRepository.saveFranchise(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<Franchise> response = franchiseUseCase.updateFranchiseName(idFranchise, newName);

        StepVerifier.create(response)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals("Mi Banco", res.getName());
                })
                .verifyComplete();

        verify(franchiseRepository, times(1)).findFranchiseById(anyLong());
        verify(franchiseRepository, times(1)).saveFranchise(any(Franchise.class));
    }

    @Test
    void updateFranchiseNameReturnExceptionWhenFranchiseNotFound() {
        when(franchiseRepository.findFranchiseById(anyLong())).thenReturn(Mono.empty());

        Mono<Franchise> response = franchiseUseCase.updateFranchiseName(idFranchise, newName);

        StepVerifier.create(response)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepository, times(1)).findFranchiseById(anyLong());
        verify(franchiseRepository, times(0)).saveFranchise(any(Franchise.class));
    }
}
