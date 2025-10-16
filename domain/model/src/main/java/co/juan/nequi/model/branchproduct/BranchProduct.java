package co.juan.nequi.model.branchproduct;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BranchProduct {

    private Long id;
    private Long idBranch;
    private Long idProduct;
    private Long stock;
}
