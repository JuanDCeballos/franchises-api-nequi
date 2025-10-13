package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.product.BranchProductRequestDto;
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
        Long idProduct = Long.valueOf(serverRequest.pathVariable("idProduct"));
        Long idBranch = Long.valueOf(serverRequest.pathVariable("idBranch"));

        return branchProductUseCase.deleteProductFromBranch(idBranch, idProduct)
                .then(ServerResponse.noContent().build());
    }
}
