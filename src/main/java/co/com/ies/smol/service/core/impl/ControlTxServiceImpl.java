package co.com.ies.smol.service.core.impl;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.domain.core.ControlTxDomainImpl;
import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.ContractService;
import co.com.ies.smol.service.ControlInterfaceBoardService;
import co.com.ies.smol.service.InterfaceBoardService;
import co.com.ies.smol.service.OperatorService;
import co.com.ies.smol.service.PurchaseOrderService;
import co.com.ies.smol.service.ReceptionOrderService;
import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.criteria.ControlInterfaceBoardCriteria;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardAssociationResponseDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import co.com.ies.smol.service.dto.core.sub.ContractSubDTO;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ControlTxServiceImpl extends ControlTxDomainImpl implements ControlTxService {

    private final Logger log = LoggerFactory.getLogger(ControlTxServiceImpl.class);

    private final ReceptionOrderService receptionOrderService;
    private final InterfaceBoardService interfaceBoardService;
    private final ControlInterfaceBoardService controlInterfaceBoardService;
    private final ContractService contractService;
    private final OperatorService operatorService;
    private final PurchaseOrderService purchaseOrderService;

    public ControlTxServiceImpl(
        ReceptionOrderService receptionOrderService,
        InterfaceBoardService interfaceBoardService,
        ControlInterfaceBoardService controlInterfaceBoardService,
        ContractService contractService,
        OperatorService operatorService,
        PurchaseOrderService purchaseOrderService
    ) {
        this.receptionOrderService = receptionOrderService;
        this.interfaceBoardService = interfaceBoardService;
        this.controlInterfaceBoardService = controlInterfaceBoardService;
        this.contractService = contractService;
        this.operatorService = operatorService;
        this.purchaseOrderService = purchaseOrderService;
    }

    @Override
    public String createBoardRegister(BoardRegisterDTO boardRegisterDTO) throws ControlTxException {
        Long purchaseOrder = boardRegisterDTO.getIesOrderNumber();
        Optional<PurchaseOrderDTO> oPurchaseOrderDTO = purchaseOrderService.getPurchaseOrderByIesOrderNumber(purchaseOrder);
        PurchaseOrderDTO purchaseOrderDTO = validateExistingPurchaseOrderAndGet(oPurchaseOrderDTO);

        List<ReceptionOrderDTO> receptionOrderList = receptionOrderService.getReceptionOrderByIesOrderNumber(purchaseOrder);

        Long boardReceived = receptionOrderList.stream().mapToLong(ReceptionOrderDTO::getAmountReceived).sum();
        Long boardToRegister = boardRegisterDTO.getAmountReceived();

        Long boardHypotheticalTotal = boardReceived + boardToRegister;
        Long boardOrderIes = purchaseOrderDTO.getOrderAmount();
        validateAvailability(boardOrderIes, boardHypotheticalTotal);

        List<String> macs = boardRegisterDTO.getMacs();
        validateIncomingBoardSize(boardToRegister, Long.valueOf(macs.size()));
        ReceptionOrderDTO receptionOrderDTO = createReceptionOrder(boardRegisterDTO, purchaseOrderDTO);

        List<InterfaceBoardDTO> existingInterfaces = new ArrayList<>();

        for (String mac : macs) {
            Optional<InterfaceBoardDTO> oInterfaceBoardDTO = interfaceBoardService.getInterfaceBoardByMac(mac);

            if (oInterfaceBoardDTO.isPresent()) {
                existingInterfaces.add(oInterfaceBoardDTO.get());
                continue;
            }
            InterfaceBoardDTO interfaceBoardDTO = createInterfaceBoard(mac, receptionOrderDTO);

            ControlInterfaceBoardDTO controlInterfaceBoardDTO = createControlInterfaceBoard(
                Location.IES,
                StatusInterfaceBoard.STOCK,
                interfaceBoardDTO,
                null
            );
            controlInterfaceBoardService.save(controlInterfaceBoardDTO);
        }

        if (!existingInterfaces.isEmpty()) {
            Long trueAmount = receptionOrderDTO.getAmountReceived() - existingInterfaces.size();
            if (trueAmount == 0) {
                deleteReceptionOrderById(receptionOrderDTO.getId());
            } else {
                receptionOrderDTO.setAmountReceived(trueAmount);
                updateReceptionOrder(receptionOrderDTO);
            }
        }

        return buildResponse(existingInterfaces);
    }

    private void deleteReceptionOrderById(Long receptionOrderId) {
        receptionOrderService.delete(receptionOrderId);
    }

    protected ReceptionOrderDTO createReceptionOrder(BoardRegisterDTO boardRegisterDTO, PurchaseOrderDTO purchaseOrderDTO) {
        ReceptionOrderDTO receptionOrderDTO = new ReceptionOrderDTO();
        receptionOrderDTO.setProviderLotNumber(boardRegisterDTO.getProviderLotNumber());
        receptionOrderDTO.setAmountReceived(boardRegisterDTO.getAmountReceived());
        receptionOrderDTO.setRemission(boardRegisterDTO.getRemission());
        ZonedDateTime currentTime = ZonedDateTime.now();
        receptionOrderDTO.setEntryDate(currentTime);
        receptionOrderDTO.setWarrantyDate(currentTime.plusYears(1));
        receptionOrderDTO.setPurchaseOrder(purchaseOrderDTO);

        return receptionOrderService.save(receptionOrderDTO);
    }

    protected ReceptionOrderDTO updateReceptionOrder(ReceptionOrderDTO receptionOrderDTO) {
        return receptionOrderService.save(receptionOrderDTO);
    }

    protected InterfaceBoardDTO createInterfaceBoard(String mac, ReceptionOrderDTO receptionOrder) {
        InterfaceBoardDTO interfaceBoardDTO = new InterfaceBoardDTO();
        interfaceBoardDTO.setMac(mac);
        interfaceBoardDTO.setReceptionOrder(receptionOrder);

        interfaceBoardDTO = interfaceBoardService.save(interfaceBoardDTO);

        return interfaceBoardDTO;
    }

    protected ControlInterfaceBoardDTO createControlInterfaceBoard(
        Location location,
        StatusInterfaceBoard status,
        InterfaceBoardDTO interfaceBoardDTO,
        ContractDTO contract
    ) {
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = new ControlInterfaceBoardDTO();
        controlInterfaceBoardDTO.setLocation(location);
        controlInterfaceBoardDTO.setState(status);
        controlInterfaceBoardDTO.setStartTime(ZonedDateTime.now());
        controlInterfaceBoardDTO.setInterfaceBoard(interfaceBoardDTO);
        controlInterfaceBoardDTO.setContract(contract);

        return controlInterfaceBoardDTO;
    }

    @Transactional
    @Override
    public void assignInterfaceBoard(AssignBoardDTO assignBoardDTO) throws ControlTxException {
        List<String> macs = assignBoardDTO.getMacs();
        String reference = assignBoardDTO.getReference();

        for (String mac : macs) {
            Optional<InterfaceBoardDTO> oInterfaceBoardDTO = interfaceBoardService.getInterfaceBoardByMac(mac);

            InterfaceBoardDTO interfaceBoardDTO = validateExistingInterfaceBoard(oInterfaceBoardDTO);
            InterfaceBoard interfaceBoard = interfaceBoardService.toEntity(interfaceBoardDTO);

            ContractType contractType = assignBoardDTO.getContractType();
            Optional<ContractDTO> oContract = contractService.getContractByReferenceAndType(reference, contractType);
            ContractDTO contract = validateExistingContract(oContract);

            Long contractedBoard = contract.getAmountInterfaceBoard();
            List<ControlInterfaceBoardDTO> assigenedControlBoardList = controlInterfaceBoardService.getControlInterfaceBoardByContractId(
                contract.getId()
            );
            Long assigenedControlBoard = Long.valueOf(assigenedControlBoardList.size());

            if (contractedBoard.equals(assigenedControlBoard)) {
                throw new ControlTxException("No es posible asignar una nueva tarjeta a este contrato");
            }
            Optional<ControlInterfaceBoardDTO> oControlInterfaceBoardDTO = controlInterfaceBoardService.getControlInterfaceBoardByInterfaceBoard(
                interfaceBoard
            );

            ControlInterfaceBoardDTO controlInterfaceBoardDTO = validateExistingBoardControl(oControlInterfaceBoardDTO);

            controlInterfaceBoardDTO.setFinishTime(ZonedDateTime.now());
            controlInterfaceBoardService.save(controlInterfaceBoardDTO);

            ControlInterfaceBoardDTO controlInterfaceBoardNewDTO = createControlInterfaceBoard(
                Location.CLIENT,
                StatusInterfaceBoard.OPERATION,
                interfaceBoardDTO,
                contract
            );
            controlInterfaceBoardService.save(controlInterfaceBoardNewDTO);
        }
    }

    @Override
    public List<InterfaceBoardDTO> getInterfaceBoardByBrand(String brandName) {
        List<Operator> operators = operatorService.findAllOperatorsByBrandName(brandName);
        List<ContractDTO> contractList = contractService.findAllContractByOpeatorIn(operators);
        List<Long> contractIds = contractList.stream().map(ContractDTO::getId).toList();
        List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByContractIds(
            contractIds
        );

        return controlInterfaceBoardList.stream().map(ControlInterfaceBoardDTO::getInterfaceBoard).toList();
    }

    @Override
    public Long getCountInterfaceBoardByBrand(String brandName) {
        List<Operator> operators = operatorService.findAllOperatorsByBrandName(brandName);
        List<ContractDTO> contractList = contractService.findAllContractByOpeatorIn(operators);

        return contractList.stream().mapToLong(ContractDTO::getAmountInterfaceBoard).sum();
    }

    @Override
    public List<InterfaceBoardDTO> getInterfaceBoardAssignedByContractAndType(String reference, ContractType contractType)
        throws ControlTxException {
        List<ControlInterfaceBoardDTO> controlInterfaceBoardList;

        if (Objects.isNull(contractType)) {
            controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByReference(reference);
        } else {
            Optional<ContractDTO> oContract = contractService.getContractByReferenceAndType(reference, contractType);
            ContractDTO contract = validateExistingContract(oContract);
            controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByContractId(contract.getId());
        }

        return controlInterfaceBoardList.stream().map(ControlInterfaceBoardDTO::getInterfaceBoard).toList();
    }

    @Override
    public Long getCountInterfaceBoardByContracted(String reference) throws ControlTxException {
        List<ContractDTO> contractList = contractService.getContractByReference(reference);

        return contractList.stream().mapToLong(ContractDTO::getAmountInterfaceBoard).sum();
    }

    @Override
    public List<InterfaceBoardDTO> getInterfaceBoardAssignedByContract(String reference) throws ControlTxException {
        List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByReference(
            reference
        );

        return controlInterfaceBoardList.stream().map(ControlInterfaceBoardDTO::getInterfaceBoard).toList();
    }

    @Override
    public Long getCountInterfaceBoardByContractedAndType(String reference, ContractType contractType) throws ControlTxException {
        Optional<ContractDTO> oContract = contractService.getContractByReferenceAndType(reference, contractType);
        ContractDTO contract = validateExistingContract(oContract);

        return contract.getAmountInterfaceBoard();
    }

    @Override
    public BoardAssociationResponseDTO getInfoBoardAssociation(Long operatorId) throws ControlTxException {
        Optional<OperatorDTO> oOperator = operatorService.findOne(operatorId);
        validateExistingOperator(oOperator);
        List<ContractDTO> contractList = contractService.getContractByOperatorId(operatorId);

        List<ContractSubDTO> contractSubList = new ArrayList<>();

        contractList.forEach(contract -> {
            List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByContractId(
                contract.getId()
            );
            log.warn(
                "-------tarjetas contratadas {} - cantidad de tarjetas {} asociada al contrato  #  {} ",
                contract.getAmountInterfaceBoard(),
                controlInterfaceBoardList.size(),
                contract.getType()
            );
            ContractSubDTO contractSub = new ContractSubDTO(
                contract.getAmountInterfaceBoard(),
                controlInterfaceBoardList.size(),
                contract.getType()
            );
            contractSubList.add(contractSub);
        });

        return new BoardAssociationResponseDTO(contractSubList);
    }

    @Override
    public Page<InterfaceBoardDTO> getInfoBoardsAvailable(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        return controlInterfaceBoardService.getInfoBoardsAvailable(pageable).map(ControlInterfaceBoardDTO::getInterfaceBoard);
    }

    @Override
    public List<InterfaceBoardDTO> getInfoBoardsByOperatorIdAndState(Long operatorId, StatusInterfaceBoard state) {
        List<ContractDTO> contractList = contractService.getContractByOperatorId(operatorId);

        List<Long> contractIdList = contractList.stream().map(ContractDTO::getId).toList();

        List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getByContractIdInAndState(
            contractIdList,
            state
        );

        return controlInterfaceBoardList.stream().map(ControlInterfaceBoardDTO::getInterfaceBoard).toList();
    }

    @Override
    public Page<ControlInterfaceBoardDTO> getControlInterfaceBoardAvailable(ControlInterfaceBoardCriteria criteria, Pageable pageable) {
        return controlInterfaceBoardService.getControlInterfaceBoardAvailable(criteria, pageable);
    }
}
