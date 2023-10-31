package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.PurchaseOrder;
import co.com.ies.smol.domain.ReceptionOrder;
import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReceptionOrder} and its DTO {@link ReceptionOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReceptionOrderMapper extends EntityMapper<ReceptionOrderDTO, ReceptionOrder> {
    @Mapping(target = "purchaseOrder", source = "purchaseOrder", qualifiedByName = "purchaseOrderIesOrderNumber")
    ReceptionOrderDTO toDto(ReceptionOrder s);

    @Named("purchaseOrderIesOrderNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "iesOrderNumber", source = "iesOrderNumber")
    @Mapping(target = "orderAmount", source = "orderAmount")
    PurchaseOrderDTO toDtoPurchaseOrderIesOrderNumber(PurchaseOrder purchaseOrder);
}
