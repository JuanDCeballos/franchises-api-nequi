package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiErrorResponse;
import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.franchise.FranchiseRequestDto;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranchiseRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/franchise",
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "listenPOSTSaveFranchise",
                    operation = @Operation(
                            operationId = "saveFranchise",
                            summary = "Endpoint to save a franchise",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Franchise information",
                                    content = @Content(schema = @Schema(implementation = FranchiseRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Franchise successfully saved",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "An error occurred while trying to save a franchise",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/franchise/{idFranchise}/top-stock",
                    method = RequestMethod.GET,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "listenGETTopProductStockByBranch",
                    operation = @Operation(
                            operationId = "getTopProductStockByBranch",
                            summary = "Endpoint to get top product stock by branch",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "idFranchise",
                                            description = "Id of a franchise",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Top product stock got successfully",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "An error occurred while trying to get the top product stock",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/franchise/{idFranchise}/name",
                    method = RequestMethod.PATCH,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "listenPATCHFranchiseName",
                    operation = @Operation(
                            operationId = "updateFranchiseName",
                            summary = "Update franchise name",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "idFranchise",
                                            description = "Id of a franchise",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Product name updated successfully",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "An error occurred while trying to update products name",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFranchiseFunction(FranchiseHandler franchiseHandler) {
        return route(POST("/api/v1/franchise"), franchiseHandler::listenPOSTSaveFranchise)
                .andRoute(GET("/api/v1/franchise/{idFranchise}/top-stock"), franchiseHandler::listenGETTopProductStockByBranch)
                .andRoute(PATCH("/api/v1/franchise/{idFranchise}/name"), franchiseHandler::listenPATCHFranchiseName);
    }
}
