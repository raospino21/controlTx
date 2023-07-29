package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Contract;
import co.com.ies.smol.domain.ControlInterfaceBoard;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ControlInterfaceBoard} and its DTO {@link ControlInterfaceBoardDTO}.
 */
@Mapper(componentModel = "spring")
public interface ControlInterfaceBoardMapper extends EntityMapper<ControlInterfaceBoardDTO, ControlInterfaceBoard> {
    @Mapping(target = "contract", source = "contract", qualifiedByName = "contractReference")
    @Mapping(target = "interfaceBoard", source = "interfaceBoard", qualifiedByName = "interfaceBoardMac")
    ControlInterfaceBoardDTO toDto(ControlInterfaceBoard s);

    @Named("contractReference")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "reference", source = "reference")
    ContractDTO toDtoContractReference(Contract contract);

    @Named("interfaceBoardMac")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "mac", source = "mac")
    InterfaceBoardDTO toDtoInterfaceBoardMac(InterfaceBoard interfaceBoard);
}
