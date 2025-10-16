package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.branch.BranchRequestDto;
import co.juan.nequi.api.dto.branch.UpdateBranchNameRequestDto;
import co.juan.nequi.api.mapper.BranchMapper;
import co.juan.nequi.api.validation.ValidationService;
import co.juan.nequi.enums.OperationMessages;
import co.juan.nequi.usecase.branch.BranchUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.juan.nequi.api.constants.ApiConstants.ID_BRANCH_PATH_VARIABLE;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
@Slf4j
public class BranchHandler {

    private final BranchUseCase branchUseCase;
    private final ValidationService validationService;
    private final BranchMapper branchMapper;

    public Mono<ServerResponse> listenPOSTSaveBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchRequestDto.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req ->
                        log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .map(branchMapper::toBranch)
                .flatMap(branchUseCase::saveBranch)
                .doOnNext(savedBranch ->
                        log.info(OperationMessages.ENTITY_SAVED_SUCCESSFULLY.getMessage(), savedBranch.toBuilder()))
                .map(branchMapper::toBranchResponseDto)
                .flatMap(savedBranch ->
                        status(201)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(savedBranch))
                );
    }

    public Mono<ServerResponse> listenPATCHBranchName(ServerRequest serverRequest) {
        Long idBranch = Long.valueOf(serverRequest.pathVariable(ID_BRANCH_PATH_VARIABLE));

        return serverRequest.bodyToMono(UpdateBranchNameRequestDto.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req ->
                        log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .flatMap(validatedDto ->
                        branchUseCase.updateBranchName(idBranch, validatedDto.getName()))
                .doOnNext(updatedBranch ->
                        log.info(OperationMessages.ENTITY_UPDATED_SUCCESSFULLY.getMessage(), updatedBranch.toBuilder()))
                .flatMap(updatedBranch ->
                        status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(updatedBranch))
                );
    }
}
