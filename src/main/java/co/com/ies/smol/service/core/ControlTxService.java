package co.com.ies.smol.service.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardAssociationResponseDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import co.com.ies.smol.service.dto.core.FilterControlInterfaceBoard;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<InterfaceBoardDTO> getInfoBoardsAvailable(@org.springdoc.api.annotations.ParameterObject Pageable pageable);

    List<InterfaceBoardDTO> getInfoBoardsByOperatorIdAndState(Long operatorId, StatusInterfaceBoard state);

    Page<ControlInterfaceBoardDTO> getControlInterfaceBoardAvailable(FilterControlInterfaceBoard filter, Pageable pageable);

    List<ContractDTO> getPendingContractsForBoard();
}
