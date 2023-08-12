package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.ControlInterfaceBoard;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ControlInterfaceBoard} and its DTO {@link ControlInterfaceBoardDTO}.
 */
@Mapper(componentModel = "spring")
public interface ControlInterfaceBoardMapper extends EntityMapper<ControlInterfaceBoardDTO, ControlInterfaceBoard> {
    ControlInterfaceBoardDTO toDto(ControlInterfaceBoard s);
}
