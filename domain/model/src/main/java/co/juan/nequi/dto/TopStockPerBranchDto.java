package co.juan.nequi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopStockPerBranchDto {

    private Long idbranch;
    private String branchname;
    private Long idproduct;
    private String productname;
    private Long stock;
}
