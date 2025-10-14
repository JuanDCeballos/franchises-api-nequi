package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.franchise.FranchiseRequestDto;
import co.juan.nequi.api.dto.franchise.UpdateFranchiseNameRequestDto;
import co.juan.nequi.api.mapper.FranchiseMapper;
import co.juan.nequi.api.validation.ValidationService;
import co.juan.nequi.dto.TopStockPerBranchDto;
import co.juan.nequi.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;
    private final ValidationService validationService;
    private final FranchiseMapper franchiseMapper;

    public Mono<ServerResponse> listenPOSTSaveFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(FranchiseRequestDto.class)
                .flatMap(validationService::validateObject)
                .map(franchiseMapper::toFranchise)
                .flatMap(franchiseUseCase::saveFranchise)
                .map(franchiseMapper::toFranchiseResponseDto)
                .flatMap(savedFranchise ->
                        status(201)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(savedFranchise))
                );
    }

    public Mono<ServerResponse> listenGETTopProductStockByBranch(ServerRequest serverRequest) {
        Long idFranchise = Long.valueOf(serverRequest.pathVariable("idFranchise"));

        Flux<TopStockPerBranchDto> resultFlux = franchiseUseCase.findTopStockProductByBranch(idFranchise);

        Mono<ApiSuccessResponse<List<TopStockPerBranchDto>>> resultMono = resultFlux
                .collectList()
                .map(dtoList -> new ApiSuccessResponse<>(dtoList));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resultMono, ApiSuccessResponse.class);
    }

    public Mono<ServerResponse> listenPATCHFranchiseName(ServerRequest serverRequest) {
        Long idFranchise = Long.valueOf(serverRequest.pathVariable("idFranchise"));

        return serverRequest.bodyToMono(UpdateFranchiseNameRequestDto.class)
                .flatMap(validationService::validateObject)
                .flatMap(validatedDto ->
                        franchiseUseCase.updateFranchiseName(idFranchise, validatedDto.getName()))
                .flatMap(updatedFranchise ->
                        status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(updatedFranchise))
                );
    }
}
