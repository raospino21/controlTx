package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.service.dto.BrandDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record BrandCompleteInfoResponse(BrandDTO brand, Long totalAmountBoardcontracted, Long totalAmountBoardAssigned)
    implements Serializable {}
