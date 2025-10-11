package co.juan.nequi.usecase.franchise;

import co.juan.nequi.model.franchise.Franchise;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

    @InjectMocks
    FranchiseUseCase franchiseUseCase;

    @Mock
    FranchiseRepository franchiseRepository;

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
}
