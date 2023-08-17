package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.ReceptionOrder;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InterfaceBoard} and its DTO {@link InterfaceBoardDTO}.
 */
@Mapper(componentModel = "spring")
public interface InterfaceBoardMapper extends EntityMapper<InterfaceBoardDTO, InterfaceBoard> {
    @Mapping(target = "receptionOrder", source = "receptionOrder", qualifiedByName = "receptionOrderProviderLotNumber")
    InterfaceBoardDTO toDto(InterfaceBoard s);

    @Named("receptionOrderProviderLotNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "providerLotNumber", source = "providerLotNumber")
    ReceptionOrderDTO toDtoReceptionOrderProviderLotNumber(ReceptionOrder receptionOrder);
}
