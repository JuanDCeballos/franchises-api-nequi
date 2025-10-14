package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiErrorResponse;
import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.branch.BranchRequestDto;
import co.juan.nequi.api.dto.branch.UpdateBranchNameRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.juan.nequi.api.constants.BranchRoutes.SAVE_BRANCH;
import static co.juan.nequi.api.constants.BranchRoutes.UPDATE_BRANCH_NAME;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = SAVE_BRANCH,
                    method = RequestMethod.POST,
                    beanClass = BranchHandler.class,
                    beanMethod = "listenPOSTSaveBranch",
                    operation = @Operation(
                            operationId = "saveBranch",
                            summary = "Endpoint to save a branch",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Branch information",
                                    content = @Content(schema = @Schema(implementation = BranchRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Branch successfully saved",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "An error occurred while trying to save a branch",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = UPDATE_BRANCH_NAME,
                    method = RequestMethod.PATCH,
                    beanClass = BranchHandler.class,
                    beanMethod = "listenPATCHBranchName",
                    operation = @Operation(
                            operationId = "updateBranchName",
                            summary = "Endpoint to update a branch name",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "idBranch",
                                            description = "Id of a branch",
                                            required = true
                                    )
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Branch new name",
                                    content = @Content(schema = @Schema(implementation = UpdateBranchNameRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Branch name successfully updated",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "An error occurred while trying to update a branch name",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerBranchFunction(BranchHandler branchHandler) {
        return route(POST(SAVE_BRANCH), branchHandler::listenPOSTSaveBranch)
                .andRoute(PATCH(UPDATE_BRANCH_NAME), branchHandler::listenPATCHBranchName);
    }
}
