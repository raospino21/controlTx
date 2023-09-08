package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.service.dto.core.sub.ContractSubDTO;
import java.io.Serializable;
import java.util.List;

public record BoardAssociationResponseDTO(List<ContractSubDTO> contractSub) implements Serializable {}
