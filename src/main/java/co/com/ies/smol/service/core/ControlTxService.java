package co.com.ies.smol.service.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardAssociationResponseDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import java.util.List;

public interface ControlTxService {
    String createBoardRegister(BoardRegisterDTO boardRegisterDTO) throws ControlTxException;

    void assignInterfaceBoard(AssignBoardDTO assignBoardDTO) throws ControlTxException;

    List<InterfaceBoardDTO> getInterfaceBoardByBrand(String brandName);

    Long getCountInterfaceBoardByBrand(String brandName);

    List<InterfaceBoardDTO> getInterfaceBoardAssignedByContractAndType(String reference, ContractType contractType)
        throws ControlTxException;

    List<InterfaceBoardDTO> getInterfaceBoardAssignedByContract(String reference) throws ControlTxException;
    Long getCountInterfaceBoardByContracted(String reference) throws ControlTxException;

    Long getCountInterfaceBoardByContractedAndType(String reference, ContractType contractType) throws ControlTxException;

    BoardAssociationResponseDTO getInfoBoardAssociation(Long operatorId) throws ControlTxException;

    List<InterfaceBoardDTO> getInfoBoardsAvailable();
}
