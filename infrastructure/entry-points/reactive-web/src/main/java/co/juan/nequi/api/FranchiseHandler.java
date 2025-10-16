package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.franchise.FranchiseRequestDto;
import co.juan.nequi.api.dto.franchise.UpdateFranchiseNameRequestDto;
import co.juan.nequi.api.mapper.FranchiseMapper;
import co.juan.nequi.api.validation.ValidationService;
import co.juan.nequi.dto.TopStockPerBranchDto;
import co.juan.nequi.enums.OperationMessages;
import co.juan.nequi.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.juan.nequi.api.constants.FranchiseRoutes.ID_FRANCHISE_PATH_VARIABLE;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
@Slf4j
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;
    private final ValidationService validationService;
    private final FranchiseMapper franchiseMapper;

    public Mono<ServerResponse> listenPOSTSaveFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseRequestDto.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req ->
                        log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .map(franchiseMapper::toFranchise)
                .flatMap(franchiseUseCase::saveFranchise)
                .doOnNext(savedFranchise ->
                        log.info(OperationMessages.ENTITY_SAVED_SUCCESSFULLY.getMessage(), savedFranchise.toBuilder()))
                .map(franchiseMapper::toFranchiseResponseDto)
                .flatMap(savedFranchise ->
                        status(201)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(savedFranchise))
                );
    }

    public Mono<ServerResponse> listenGETTopProductStockByBranch(ServerRequest serverRequest) {
        Long idFranchise = Long.valueOf(serverRequest.pathVariable(ID_FRANCHISE_PATH_VARIABLE));

        Flux<TopStockPerBranchDto> resultFlux = franchiseUseCase.findTopStockProductByBranch(idFranchise);

        Mono<ApiSuccessResponse<List<TopStockPerBranchDto>>> resultMono = resultFlux
                .collectList()
                .map(ApiSuccessResponse::new);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resultMono, ApiSuccessResponse.class);
    }

    public Mono<ServerResponse> listenPATCHFranchiseName(ServerRequest serverRequest) {
        Long idFranchise = Long.valueOf(serverRequest.pathVariable(ID_FRANCHISE_PATH_VARIABLE));

        return serverRequest.bodyToMono(UpdateFranchiseNameRequestDto.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req ->
                        log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .flatMap(validatedDto ->
                        franchiseUseCase.updateFranchiseName(idFranchise, validatedDto.getName()))
                .doOnNext(updatedFranchise ->
                        log.info(OperationMessages.ENTITY_UPDATED_SUCCESSFULLY.getMessage(), updatedFranchise.toBuilder()))
                .flatMap(updatedFranchise ->
                        status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(updatedFranchise))
                );
    }
}
