package co.juan.nequi.usecase.franchise;

import co.juan.nequi.dto.TopStockPerBranchDto;
import co.juan.nequi.model.branch.Branch;
import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.model.branchproduct.BranchProduct;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import co.juan.nequi.model.exceptions.FranchiseNotFoundException;
import co.juan.nequi.model.exceptions.ProductNotFoundException;
import co.juan.nequi.model.franchise.Franchise;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
import co.juan.nequi.model.product.Product;
import co.juan.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
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
    private Branch branch;
    private BranchProduct branchProduct;
    private Product product;

    @BeforeEach
    void initMocks() {
        franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("MiBanco");

        branch = new Branch();
        branch.setId(1L);
        branch.setName("MiBanco - Centro");
        branch.setIdFranchise(1L);

        branchProduct = new BranchProduct();
        branchProduct.setId(1L);
        branchProduct.setIdBranch(1L);
        branchProduct.setIdProduct(1L);
        branchProduct.setStock(7L);

        product = new Product();
        product.setId(1L);
        product.setName("Pepsi");
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
    void findTopStockProductByBranch() {
        when(franchiseRepository.existsFranchiseById(anyLong())).thenReturn(Mono.just(true));
        when(branchRepository.findBranchByIdFranchise(anyLong())).thenReturn(Flux.just(branch));
        when(branchProductRepository.findTopStockByIdBranch(anyLong())).thenReturn(Mono.just(branchProduct));
        when(productRepository.findProductById(anyLong())).thenReturn(Mono.just(product));

        Flux<TopStockPerBranchDto> response = franchiseUseCase.findTopStockProductByBranch(idFranchise);

        StepVerifier.create(response)
                .assertNext(fluxRes -> {
                    assertNotNull(fluxRes);
                    assertEquals(1L, fluxRes.getIdBranch());
                    assertEquals("MiBanco - Centro", fluxRes.getBranchName());
                    assertEquals(1L, fluxRes.getIdProduct());
                    assertEquals("Pepsi", fluxRes.getProductName());
                    assertEquals(7L, fluxRes.getStock());
                })
                .verifyComplete();

        verify(franchiseRepository, times(1)).existsFranchiseById(anyLong());
        verify(branchRepository, times(1)).findBranchByIdFranchise(anyLong());
        verify(branchProductRepository, times(1)).findTopStockByIdBranch(anyLong());
        verify(productRepository, times(1)).findProductById(anyLong());
    }

    @Test
    void findTopStockProductByBranchReturnExceptionWhenFranchiseNotFound() {
        when(franchiseRepository.existsFranchiseById(anyLong())).thenReturn(Mono.just(false));

        Flux<TopStockPerBranchDto> response = franchiseUseCase.findTopStockProductByBranch(idFranchise);

        StepVerifier.create(response)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepository, times(1)).existsFranchiseById(anyLong());
        verify(branchRepository, times(0)).findBranchByIdFranchise(anyLong());
        verify(branchProductRepository, times(0)).findTopStockByIdBranch(anyLong());
        verify(productRepository, times(0)).findProductById(anyLong());
    }

    @Test
    void findTopStockProductByBranchReturnExceptionWhenProductNotFound() {
        when(franchiseRepository.existsFranchiseById(anyLong())).thenReturn(Mono.just(true));
        when(branchRepository.findBranchByIdFranchise(anyLong())).thenReturn(Flux.just(branch));
        when(branchProductRepository.findTopStockByIdBranch(anyLong())).thenReturn(Mono.just(branchProduct));
        when(productRepository.findProductById(anyLong())).thenReturn(Mono.empty());

        Flux<TopStockPerBranchDto> response = franchiseUseCase.findTopStockProductByBranch(idFranchise);

        StepVerifier.create(response)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(franchiseRepository, times(1)).existsFranchiseById(anyLong());
        verify(branchRepository, times(1)).findBranchByIdFranchise(anyLong());
        verify(branchProductRepository, times(1)).findTopStockByIdBranch(anyLong());
        verify(productRepository, times(1)).findProductById(anyLong());
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
