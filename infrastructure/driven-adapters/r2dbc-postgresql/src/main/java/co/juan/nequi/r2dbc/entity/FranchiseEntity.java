package co.juan.nequi.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("franchises")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FranchiseEntity {

    @Id
    private Long id;

    private String name;
}
