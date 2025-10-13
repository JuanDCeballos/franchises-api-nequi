package co.juan.nequi.api.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductStockRequestDto {

    @NotNull(message = "Product stock is required.")
    @Min(value = 0, message = "Stock cannot be negative.")
    private Long stock;
}
