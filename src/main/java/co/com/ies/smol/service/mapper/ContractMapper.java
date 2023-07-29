package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Contract;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Mapping(target = "operator", source = "operator", qualifiedByName = "operatorName")
    ContractDTO toDto(Contract s);

    @Named("operatorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    OperatorDTO toDtoOperatorName(Operator operator);
}
