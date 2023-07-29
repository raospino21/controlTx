package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Brand;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.service.dto.BrandDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Operator} and its DTO {@link OperatorDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperatorMapper extends EntityMapper<OperatorDTO, Operator> {
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandName")
    OperatorDTO toDto(Operator s);

    @Named("brandName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BrandDTO toDtoBrandName(Brand brand);
}
