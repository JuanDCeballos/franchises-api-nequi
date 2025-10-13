package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiErrorResponse;
import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.product.BranchProductRequestDto;
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
public class ProductRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/product",
                    method = RequestMethod.POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "listenPOSTSaveProduct",
                    operation = @Operation(
                            operationId = "saveProduct",
                            summary = "Endpoint to save a product",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Product information",
                                    content = @Content(schema = @Schema(implementation = BranchProductRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Product successfully saved",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "An error occurred while trying to save a product",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerProductFunction(ProductHandler productHandler) {
        return route(POST("/api/v1/product"), productHandler::listenPOSTSaveProduct);
    }
}
