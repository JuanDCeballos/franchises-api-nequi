package co.juan.nequi.api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchProductRequestDto {

    @NotBlank(message = "Product name is required.")
    private String name;

    @NotNull(message = "Product stock is required.")
    private Long stock;

    @NotNull(message = "IdBranch is required.")
    private Long idBranch;
}
