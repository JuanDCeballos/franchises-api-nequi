package co.juan.nequi.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Franchises API",
        description = "API for handling franchises, branch offices and products"
))
public class SwaggerConfig {
}
