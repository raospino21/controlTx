package co.com.ies.smol.service.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.criteria.OperatorCriteria;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.dto.core.*;
import co.com.ies.smol.service.dto.core.sub.ContractSubDTO;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ControlTxService {
    RequestStatusRecord createBoardRegister(BoardRegisterDTO boardRegisterDTO) throws ControlTxException;

    ByteArrayInputStream assignInterfaceBoard(int amountToAssociate, String reference, ContractType contractType) throws ControlTxException;

    List<InterfaceBoardDTO> getInterfaceBoardByBrand(String brandName);

    Long getCountInterfaceBoardByBrand(String brandName);

    Page<InterfaceBoardDTO> getInterfaceBoardAssignedByContractAndType(String reference, ContractType contractType, Pageable pageable)
        throws ControlTxException;

    List<InterfaceBoardDTO> getInterfaceBoardAssignedByContract(String reference) throws ControlTxException;
    Long getCountInterfaceBoardByContracted(String reference) throws ControlTxException;

    Long getCountInterfaceBoardByContractedAndType(String reference, ContractType contractType) throws ControlTxException;

    BoardAssociationResponseDTO getInfoBoardAssociation(Long operatorId) throws ControlTxException;

    Page<InterfaceBoardDTO> getInfoBoardsAvailable(String mac, @org.springdoc.api.annotations.ParameterObject Pageable pageable)
        throws ControlTxException;

    List<InterfaceBoardDTO> getInfoBoardsByOperatorIdAndState(Long operatorId, StatusInterfaceBoard state);

    Page<ControlInterfaceBoardDTO> getControlInterfaceBoardAvailable(FilterControlInterfaceBoard filter, Pageable pageable)
        throws ControlTxException;

    List<ContractDTO> getPendingContractsForBoard();

    List<ReceptionOrderDTO> getPendingReceptionOrderForBoard();

    List<PurchaseOrderDTO> getPendingPurchaseOrderForReceptionOrder();

    ReceptionOrderDTO saveReceptionOrder(ReceptionOrderDTO receptionOrderDTO) throws ControlTxException;

    Page<PurchaseOrderCompleteResponse> getAllPurchaseOrdersComplete(Pageable pageable);

    Page<BrandCompleteInfoResponse> getCompleteInfoBrands(Pageable pageable);

    Page<OperatorCompleteInfoResponse> getCompleteInfoOperators(OperatorCriteria criteria, Pageable pageable);

    ByteArrayInputStream getFileWithOperatorBoardsByContractId(Long contractId);
    List<ContractSubDTO> getInfoBoardAssociationByReference(String reference);

    Integer getCountBoardsAvailable();
}
