package co.juan.nequi.api;

import co.juan.nequi.api.dto.ApiErrorResponse;
import co.juan.nequi.api.dto.ApiSuccessResponse;
import co.juan.nequi.api.dto.product.BranchProductRequestDto;
import co.juan.nequi.api.dto.product.UpdateProductStockRequestDto;
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
            ),
            @RouterOperation(
                    path = "/api/v1/product/{idProduct}/branch/{idBranch}/delete",
                    method = RequestMethod.DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = "listenDELETEProductFromBranch",
                    operation = @Operation(
                            operationId = "deleteProduct",
                            summary = "Deletes a product from a specific branch",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "idProduct",
                                            description = "Id of a product",
                                            required = true
                                    ),
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "idBranch",
                                            description = "Id of a branch",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Product successfully removed"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "An error occurred while trying to delete a product",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/product/{idProduct}/branch/{idBranch}/stock",
                    method = RequestMethod.PATCH,
                    beanClass = ProductHandler.class,
                    beanMethod = "listenPATCHUpdateProduct",
                    operation = @Operation(
                            operationId = "updateStock",
                            summary = "Update product stock",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "idProduct",
                                            description = "Id of a product",
                                            required = true
                                    ),
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "idBranch",
                                            description = "Id of a branch",
                                            required = true
                                    )
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Stock",
                                    content = @Content(schema = @Schema(implementation = UpdateProductStockRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Stock successfully updated",
                                            content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "An error occurred while trying to update a product stock",
                                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerProductFunction(ProductHandler productHandler) {
        return route(POST("/api/v1/product"), productHandler::listenPOSTSaveProduct)
                .andRoute(DELETE("/api/v1/product/{idProduct}/branch/{idBranch}/delete"), productHandler::listenDELETEProductFromBranch)
                .andRoute(PATCH("/api/v1/product/{idProduct}/branch/{idBranch}/stock"), productHandler::listenPATCHUpdateProduct);
    }
}
