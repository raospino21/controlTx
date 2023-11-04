package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.service.dto.BrandDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class BrandCompleteInfoResponse implements Serializable {

    private BrandDTO brand;

    private Long totalAmountBoardcontracted;

    private Long totalAmountBoardAssigned;

    List<OperatorDTO> operators;
}
