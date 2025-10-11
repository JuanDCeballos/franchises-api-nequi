package co.juan.nequi.api.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchResponseDto {

    private Long id;
    private String name;
    private Long idFranchise;
}
