package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Brand;
import co.com.ies.smol.service.dto.BrandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "spring")
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {}
