package co.juan.nequi.api.mapper;

import co.juan.nequi.api.dto.product.BranchProductRequestDto;
import co.juan.nequi.api.dto.product.BranchProductResponseDto;
import co.juan.nequi.dto.BranchProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    BranchProductDto toBranchProduct(BranchProductRequestDto branchProductRequestDto);

    BranchProductResponseDto toProductResponseDto(BranchProductDto product);
}
