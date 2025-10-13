package co.juan.nequi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BranchProductDto {

    private Long id;
    private String name;
    private Long stock;
    private Long idBranch;
}
