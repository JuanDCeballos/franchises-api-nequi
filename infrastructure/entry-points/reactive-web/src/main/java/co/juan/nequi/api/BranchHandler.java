package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.branch.BranchRequestDto;
import co.juan.nequi.api.dto.branch.UpdateBranchNameRequestDto;
import co.juan.nequi.api.mapper.BranchMapper;
import co.juan.nequi.api.validation.ValidationService;
import co.juan.nequi.usecase.branch.BranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.juan.nequi.api.constants.ApiConstants.ID_BRANCH_PATH_VARIABLE;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchUseCase branchUseCase;
    private final ValidationService validationService;
    private final BranchMapper branchMapper;

    public Mono<ServerResponse> listenPOSTSaveBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BranchRequestDto.class)
                .flatMap(validationService::validateObject)
                .map(branchMapper::toBranch)
                .flatMap(branchUseCase::saveBranch)
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
                .flatMap(validatedDto ->
                        branchUseCase.updateBranchName(idBranch, validatedDto.getName()))
                .flatMap(updatedBranch ->
                        status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ApiSuccessResponse<>(updatedBranch))
                );
    }
}
