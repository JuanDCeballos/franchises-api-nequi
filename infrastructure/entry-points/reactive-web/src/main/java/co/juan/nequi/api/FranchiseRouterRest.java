package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiErrorResponse;
import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.franchise.FranchiseRequestDto;
import io.swagger.v3.oas.annotations.Operation;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
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
            )
    })
    public RouterFunction<ServerResponse> routerFranchiseFunction(FranchiseHandler franchiseHandler) {
        return route(POST("/api/v1/franchise"), franchiseHandler::listenPOSTSaveFranchise);
    }
}
