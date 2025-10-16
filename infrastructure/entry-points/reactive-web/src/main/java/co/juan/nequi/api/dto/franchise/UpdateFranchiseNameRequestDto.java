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
public class UpdateFranchiseNameRequestDto {

    @NotBlank(message = "Franchise name is required.")
    private String name;
}
