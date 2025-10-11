package co.juan.nequi.api.mapper;

import co.juan.nequi.api.dto.franchise.FranchiseRequestDto;
import co.juan.nequi.api.dto.franchise.FranchiseResponseDto;
import co.juan.nequi.model.franchise.Franchise;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    Franchise toFranchise(FranchiseRequestDto franchiseRequestDto);

    FranchiseResponseDto toFranchiseResponseDto(Franchise franchise);
}
