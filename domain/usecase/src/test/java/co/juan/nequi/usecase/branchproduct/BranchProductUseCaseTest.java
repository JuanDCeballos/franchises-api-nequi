package co.juan.nequi.usecase.branchproduct;

import co.juan.nequi.exceptions.BranchProductRelationException;
import co.juan.nequi.model.branchproduct.BranchProduct;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
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
class BranchProductUseCaseTest {

    @InjectMocks
    BranchProductUseCase branchProductUseCase;

    @Mock
    BranchProductRepository branchProductRepository;

    private BranchProduct branchProduct;
    private final Long idBranch = 1L;
    private final Long idProduct = 1L;
    private final Long newStock = 9L;

    @BeforeEach
    void initMocks() {
        branchProduct = new BranchProduct();
        branchProduct.setId(1L);
        branchProduct.setIdBranch(1L);
        branchProduct.setIdProduct(1L);
        branchProduct.setStock(5L);
    }

    @Test
    void saveBranchProduct() {
        when(branchProductRepository.saveBranchProduct(any(BranchProduct.class))).thenReturn(Mono.just(branchProduct));

        Mono<BranchProduct> response = branchProductUseCase.saveBranchProduct(branchProduct);

        StepVerifier.create(response)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals(branchProduct.getId(), res.getId());
                    assertEquals(branchProduct.getStock(), res.getStock());
                })
                .verifyComplete();

        verify(branchProductRepository, times(1)).saveBranchProduct(any(BranchProduct.class));
    }

    @Test
    void deleteProductFromBranch() {
        when(branchProductRepository.findRelationByIdBranchAndIdProduct(
                anyLong(), anyLong())).thenReturn(Mono.just(branchProduct));
        when(branchProductRepository.deleteProductFromBranch(anyLong())).thenReturn(Mono.empty());

        Mono<Void> response = branchProductUseCase.deleteProductFromBranch(idBranch, idProduct);

        StepVerifier.create(response)
                .verifyComplete();

        verify(branchProductRepository, times(1)).findRelationByIdBranchAndIdProduct(
                anyLong(), anyLong());
        verify(branchProductRepository, times(1)).deleteProductFromBranch(anyLong());
    }

    @Test
    void deleteProductFromBranchReturnExceptionWhenRelationDoesNotExists() {
        when(branchProductRepository.findRelationByIdBranchAndIdProduct(
                anyLong(), anyLong())).thenReturn(Mono.empty());

        Mono<Void> response = branchProductUseCase.deleteProductFromBranch(idBranch, idProduct);

        StepVerifier.create(response)
                .expectError(BranchProductRelationException.class)
                .verify();

        verify(branchProductRepository, times(1)).findRelationByIdBranchAndIdProduct(
                anyLong(), anyLong());
        verify(branchProductRepository, times(0)).deleteProductFromBranch(anyLong());
    }

    @Test
    void updateProductStock() {
        when(branchProductRepository.findRelationByIdBranchAndIdProduct(
                anyLong(), anyLong())).thenReturn(Mono.just(branchProduct));
        when(branchProductRepository.saveBranchProduct(any(BranchProduct.class))).thenReturn(Mono.just(branchProduct));

        Mono<BranchProduct> response = branchProductUseCase.updateProductStock(idBranch, idProduct, newStock);

        StepVerifier.create(response)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals(newStock, res.getStock());
                })
                .verifyComplete();

        verify(branchProductRepository, times(1)).findRelationByIdBranchAndIdProduct(
                anyLong(), anyLong());
        verify(branchProductRepository, times(1)).saveBranchProduct(any(BranchProduct.class));
    }

    @Test
    void updateProductStockReturnExceptionWhenRelationDoesNotExists() {
        when(branchProductRepository.findRelationByIdBranchAndIdProduct(anyLong(), anyLong())).thenReturn(Mono.empty());

        Mono<BranchProduct> response = branchProductUseCase.updateProductStock(idBranch, idProduct, newStock);

        StepVerifier.create(response)
                .expectError(BranchProductRelationException.class)
                .verify();

        verify(branchProductRepository, times(1)).findRelationByIdBranchAndIdProduct(
                anyLong(), anyLong());
        verify(branchProductRepository, times(0)).saveBranchProduct(any(BranchProduct.class));
    }
}
