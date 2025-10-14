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

    private Long idBranch;
    private String branchName;
    private Long idProduct;
    private String productName;
    private Long stock;
}
