package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.service.dto.BrandDTO;
import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record BrandCompleteInfoResponse(BrandDTO brand, Long totalAmountBoardcontracted, Long totalAmountBoardAssigned)
    implements Serializable {}
