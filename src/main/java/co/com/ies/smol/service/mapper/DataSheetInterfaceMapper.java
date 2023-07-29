package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.DataSheetInterface;
import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DataSheetInterface} and its DTO {@link DataSheetInterfaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DataSheetInterfaceMapper extends EntityMapper<DataSheetInterfaceDTO, DataSheetInterface> {}
