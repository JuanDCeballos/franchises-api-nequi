package co.juan.nequi.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("branches")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BranchEntity {

    @Id
    private Long id;

    private String name;

    @Column("id_franchise")
    private Long idFranchise;
}
