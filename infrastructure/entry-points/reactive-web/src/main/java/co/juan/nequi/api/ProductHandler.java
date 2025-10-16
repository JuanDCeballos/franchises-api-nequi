package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.product.BranchProductRequestDto;
import co.juan.nequi.api.dto.product.UpdateProductNameRequestDto;
import co.juan.nequi.api.dto.product.UpdateProductStockRequestDto;
import co.juan.nequi.api.mapper.ProductMapper;
import co.juan.nequi.api.validation.ValidationService;
import co.juan.nequi.enums.OperationMessages;
import co.juan.nequi.usecase.branchproduct.BranchProductUseCase;
import co.juan.nequi.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.juan.nequi.api.constants.ApiConstants.ID_BRANCH_PATH_VARIABLE;
import static co.juan.nequi.api.constants.ProductRoutes.ID_PRODUCT_PATH_VARIABLE;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductHandler {

    private final ProductUseCase productUseCase;
    private final BranchProductUseCase branchProductUseCase;
    private final ValidationService validationService;
    private final ProductMapper productMapper;

    public Mono<ServerResponse> listenPOSTSaveProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchProductRequestDto.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req ->
                        log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .map(productMapper::toBranchProduct)
                .flatMap(productUseCase::saveProduct)
                .doOnNext(savedProduct ->
                        log.info(OperationMessages.ENTITY_SAVED_SUCCESSFULLY.getMessage(), savedProduct.toBuilder()))
                .map(productMapper::toProductResponseDto)
                .flatMap(savedProduct ->
                        status(201)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(savedProduct))
                );
    }

    public Mono<ServerResponse> listenDELETEProductFromBranch(ServerRequest serverRequest) {
        Long idProduct = Long.valueOf(serverRequest.pathVariable(ID_PRODUCT_PATH_VARIABLE));
        Long idBranch = Long.valueOf(serverRequest.pathVariable(ID_BRANCH_PATH_VARIABLE));

        return branchProductUseCase.deleteProductFromBranch(idBranch, idProduct)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> listenPATCHUpdateProduct(ServerRequest serverRequest) {
        Long idProduct = Long.valueOf(serverRequest.pathVariable(ID_PRODUCT_PATH_VARIABLE));
        Long idBranch = Long.valueOf(serverRequest.pathVariable(ID_BRANCH_PATH_VARIABLE));

        return serverRequest.bodyToMono(UpdateProductStockRequestDto.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req ->
                        log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .flatMap(validatedDto ->
                        branchProductUseCase.updateProductStock(idBranch, idProduct, validatedDto.getStock()))
                .doOnNext(updatedProduct ->
                        log.info(OperationMessages.ENTITY_UPDATED_SUCCESSFULLY.getMessage(), updatedProduct.toBuilder()))
                .flatMap(updatedProduct ->
                        status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(updatedProduct))
                );
    }

    public Mono<ServerResponse> listenPATCHUpdateProductName(ServerRequest serverRequest) {
        Long idProduct = Long.valueOf(serverRequest.pathVariable(ID_PRODUCT_PATH_VARIABLE));

        return serverRequest.bodyToMono(UpdateProductNameRequestDto.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req ->
                        log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .flatMap(validatedDto ->
                        productUseCase.updateProductName(idProduct, validatedDto.getName()))
                .doOnNext(updatedProduct ->
                        log.info(OperationMessages.ENTITY_UPDATED_SUCCESSFULLY.getMessage(), updatedProduct.toBuilder()))
                .flatMap(updatedProduct ->
                        status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(updatedProduct))
                );
    }
}
