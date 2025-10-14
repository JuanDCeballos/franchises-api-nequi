package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.product.BranchProductRequestDto;
import co.juan.nequi.api.dto.product.UpdateProductNameRequestDto;
import co.juan.nequi.api.dto.product.UpdateProductStockRequestDto;
import co.juan.nequi.api.mapper.ProductMapper;
import co.juan.nequi.api.validation.ValidationService;
import co.juan.nequi.usecase.branchproduct.BranchProductUseCase;
import co.juan.nequi.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private static final String ID_PRODUCT = "idProduct";
    private static final String ID_BRANCH = "idBranch";

    private final ProductUseCase productUseCase;
    private final BranchProductUseCase branchProductUseCase;
    private final ValidationService validationService;
    private final ProductMapper productMapper;

    public Mono<ServerResponse> listenPOSTSaveProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchProductRequestDto.class)
                .flatMap(validationService::validateObject)
                .map(productMapper::toBranchProduct)
                .flatMap(productUseCase::saveProduct)
                .map(productMapper::toProductResponseDto)
                .flatMap(savedProduct ->
                        status(201)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(savedProduct))
                );
    }

    public Mono<ServerResponse> listenDELETEProductFromBranch(ServerRequest serverRequest) {
        Long idProduct = Long.valueOf(serverRequest.pathVariable(ID_PRODUCT));
        Long idBranch = Long.valueOf(serverRequest.pathVariable(ID_BRANCH));

        return branchProductUseCase.deleteProductFromBranch(idBranch, idProduct)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> listenPATCHUpdateProduct(ServerRequest serverRequest) {
        Long idProduct = Long.valueOf(serverRequest.pathVariable(ID_PRODUCT));
        Long idBranch = Long.valueOf(serverRequest.pathVariable(ID_BRANCH));

        return serverRequest.bodyToMono(UpdateProductStockRequestDto.class)
                .flatMap(validationService::validateObject)
                .flatMap(validatedDto ->
                        branchProductUseCase.updateProductStock(idBranch, idProduct, validatedDto.getStock()))
                .flatMap(updatedProduct ->
                        status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(updatedProduct))
                );
    }

    public Mono<ServerResponse> listenPATCHUpdateProductName(ServerRequest serverRequest) {
        Long idProduct = Long.valueOf(serverRequest.pathVariable(ID_PRODUCT));

        return serverRequest.bodyToMono(UpdateProductNameRequestDto.class)
                .flatMap(validationService::validateObject)
                .flatMap(validatedDto ->
                        productUseCase.updateProductName(idProduct, validatedDto.getName()))
                .flatMap(updatedProduct ->
                        status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(updatedProduct))
                );
    }
}
