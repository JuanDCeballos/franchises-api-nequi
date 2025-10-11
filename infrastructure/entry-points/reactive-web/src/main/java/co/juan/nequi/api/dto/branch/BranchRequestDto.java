package co.juan.nequi.api.dto.branch;

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
public class BranchRequestDto {

    @NotBlank(message = "Branch name is required.")
    private String name;

    @NotNull(message = "Franchise id is required.")
    private Long idFranchise;
}
