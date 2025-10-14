package co.juan.nequi.usecase.product;

import co.juan.nequi.dto.BranchProductDto;
import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.model.branchproduct.BranchProduct;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import co.juan.nequi.model.exceptions.BranchNotFoundException;
import co.juan.nequi.model.exceptions.ProductNotFoundException;
import co.juan.nequi.model.product.Product;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @InjectMocks
    ProductUseCase productUseCase;

    @Mock
    ProductRepository productRepository;

    @Mock
    BranchProductRepository branchProductRepository;

    @Mock
    BranchRepository branchRepository;

    private final Long idProduct = 1L;
    private final String newName = "DrPepper";

    private BranchProduct branchProduct;
    private BranchProductDto branchProductDto;
    private Product product;

    @BeforeEach
    void initMocks() {
        branchProduct = new BranchProduct();
        branchProduct.setId(1L);
        branchProduct.setIdBranch(1L);
        branchProduct.setIdProduct(1L);
        branchProduct.setStock(5L);

        branchProductDto = new BranchProductDto();
        branchProductDto.setId(1L);
        branchProductDto.setName("Coca-Cola");
        branchProductDto.setStock(5L);
        branchProductDto.setIdBranch(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Coca-Cola");
    }

    @Test
    void saveProduct() {
        when(branchRepository.exitsBranchById(anyLong())).thenReturn(Mono.just(true));
        when(productRepository.findProductByName(anyString())).thenReturn(Mono.just(product));
        when(productRepository.saveProduct(any(Product.class))).thenReturn(Mono.just(product));
        when(branchProductRepository.saveBranchProduct(any(BranchProduct.class))).thenReturn(Mono.just(branchProduct));

        Mono<BranchProductDto> response = productUseCase.saveProduct(branchProductDto);

        StepVerifier.create(response)
                .assertNext(dto -> {
                    assertNotNull(dto);
                    assertEquals(product.getId(), dto.getId());
                    assertEquals(product.getName(), dto.getName());
                })
                .verifyComplete();

        verify(branchRepository, times(1)).exitsBranchById(anyLong());
        verify(productRepository, times(1)).findProductByName(anyString());
        verify(productRepository, times(1)).saveProduct(any(Product.class));
        verify(branchProductRepository, times(1)).saveBranchProduct(any(BranchProduct.class));
    }

    @Test
    void saveProductReturnExceptionWhenBranchDoesNotExists() {
        when(branchRepository.exitsBranchById(anyLong())).thenReturn(Mono.just(false));

        Mono<BranchProductDto> response = productUseCase.saveProduct(branchProductDto);

        StepVerifier.create(response)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepository, times(1)).exitsBranchById(anyLong());
        verify(productRepository, times(0)).findProductByName(anyString());
        verify(productRepository, times(0)).saveProduct(any(Product.class));
        verify(branchProductRepository, times(0)).saveBranchProduct(any(BranchProduct.class));
    }

    @Test
    void updateProductName() {
        when(productRepository.findProductById(anyLong())).thenReturn(Mono.just(product));
        when(productRepository.saveProduct(any(Product.class))).thenReturn(Mono.just(product));

        Mono<Product> response = productUseCase.updateProductName(idProduct, newName);

        StepVerifier.create(response)
                .assertNext(dto -> {
                    assertNotNull(dto);
                    assertEquals("DrPepper", dto.getName());
                })
                .verifyComplete();

        verify(productRepository, times(1)).findProductById(anyLong());
        verify(productRepository, times(1)).saveProduct(any(Product.class));
    }

    @Test
    void updateProductNameReturnExceptionWhenProductNotFound() {
        when(productRepository.findProductById(anyLong())).thenReturn(Mono.empty());

        Mono<Product> response = productUseCase.updateProductName(idProduct, newName);

        StepVerifier.create(response)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepository, times(1)).findProductById(anyLong());
        verify(productRepository, times(0)).saveProduct(any(Product.class));
    }
}
