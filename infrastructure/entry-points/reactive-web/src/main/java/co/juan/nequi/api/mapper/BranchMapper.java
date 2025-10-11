package co.juan.nequi.api.mapper;

import co.juan.nequi.api.dto.branch.BranchRequestDto;
import co.juan.nequi.api.dto.branch.BranchResponseDto;
import co.juan.nequi.model.branch.Branch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toBranch(BranchRequestDto branchRequestDto);

    BranchResponseDto toBranchResponseDto(Branch branch);
}
