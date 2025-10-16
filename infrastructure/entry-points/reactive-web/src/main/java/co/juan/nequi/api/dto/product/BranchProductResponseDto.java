package co.juan.nequi.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchProductResponseDto {

    private Long id;
    private String name;
    private Long stock;
    private Long idBranch;
}
