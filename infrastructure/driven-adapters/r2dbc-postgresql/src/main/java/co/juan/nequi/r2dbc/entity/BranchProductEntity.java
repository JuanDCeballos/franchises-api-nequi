package co.juan.nequi.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("branch_product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BranchProductEntity {

    @Id
    private Long id;

    @Column("id_branch")
    private Long idBranch;

    @Column("id_product")
    private Long idProduct;

    private Long stock;
}
