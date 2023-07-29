package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.DataSheetInterface;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InterfaceBoard} and its DTO {@link InterfaceBoardDTO}.
 */
@Mapper(componentModel = "spring")
public interface InterfaceBoardMapper extends EntityMapper<InterfaceBoardDTO, InterfaceBoard> {
    @Mapping(target = "dataSheetInterface", source = "dataSheetInterface", qualifiedByName = "dataSheetInterfaceId")
    InterfaceBoardDTO toDto(InterfaceBoard s);

    @Named("dataSheetInterfaceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DataSheetInterfaceDTO toDtoDataSheetInterfaceId(DataSheetInterface dataSheetInterface);
}
