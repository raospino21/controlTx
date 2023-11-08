package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record PurchaseOrderCompleteResponse(PurchaseOrderDTO purchaseOrder, List<ReceptionOrderDTO> receptionOrderList)
    implements Serializable {}
