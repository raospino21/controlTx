package co.com.ies.smol.service.dto.core.sub;

import co.com.ies.smol.domain.enumeration.ContractType;
import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record ContractSubDTO(Long amountInterfaceBoardContracted, int amountInterfaceBoardAssigned, ContractType contractType)
    implements Serializable {}
