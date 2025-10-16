package co.juan.nequi.api.dto.franchise;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FranchiseRequestDto {

    @NotBlank(message = "Franchise name is required.")
    private String name;
}
