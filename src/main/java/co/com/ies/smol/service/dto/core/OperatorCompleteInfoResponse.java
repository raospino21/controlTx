package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.service.dto.core.sub.ContractSubDTO;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record OperatorCompleteInfoResponse(OperatorDTO operator, List<ContractSubDTO> contractSubList) implements Serializable {}
