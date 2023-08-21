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
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public void createBoardRegister(BoardRegisterDTO boardRegisterDTO) throws ControlTxException {
        List<String> macs = boardRegisterDTO.getMacs();
        ReceptionOrderDTO receptionOrderDTO = createReceptionOrder(boardRegisterDTO);

        for (String mac : macs) {
            InterfaceBoardDTO interfaceBoardDTO = createInterfaceBoard(mac, receptionOrderDTO);
            interfaceBoardDTO = interfaceBoardService.save(interfaceBoardDTO);

            ControlInterfaceBoardDTO controlInterfaceBoardDTO = createControlInterfaceBoard(
                Location.IES,
                StatusInterfaceBoard.STOCK,
                interfaceBoardDTO,
                null
            );
            controlInterfaceBoardDTO = controlInterfaceBoardService.save(controlInterfaceBoardDTO);

            log.info(
                "receptionOrderDTO ID {}  - interfaceBoardDTO ID {} - controlInterfaceBoardDTO ID {} ",
                receptionOrderDTO.getId(),
                interfaceBoardDTO.getId(),
                controlInterfaceBoardDTO.getId()
            );
        }
    }

    protected ReceptionOrderDTO createReceptionOrder(BoardRegisterDTO boardRegisterDTO) throws ControlTxException {
        ReceptionOrderDTO receptionOrderDTO = new ReceptionOrderDTO();
        receptionOrderDTO.setProviderLotNumber(boardRegisterDTO.getColcircuitosLotNumber());
        receptionOrderDTO.setAmountReceived(boardRegisterDTO.getAmountReceived());
        receptionOrderDTO.setRemission(boardRegisterDTO.getRemission());
        ZonedDateTime currentTime = ZonedDateTime.now();
        receptionOrderDTO.setEntryDate(currentTime);
        receptionOrderDTO.setWarrantyDate(currentTime.plusYears(1));
        Long purchaseOrder = boardRegisterDTO.getIesOrderNumber();
        Optional<PurchaseOrderDTO> oPurchaseOrderDTO = purchaseOrderService.getPurchaseOrderByIesOrderNumber(purchaseOrder);
        if (oPurchaseOrderDTO.isEmpty()) {
            throw new ControlTxException("Orden de compra no encontrada " + purchaseOrder);
        }
        receptionOrderDTO.setPurchaseOrder(oPurchaseOrderDTO.get());

        return receptionOrderService.save(receptionOrderDTO);
    }

    protected InterfaceBoardDTO createInterfaceBoard(String mac, ReceptionOrderDTO receptionOrder) {
        InterfaceBoardDTO interfaceBoardDTO = new InterfaceBoardDTO();
        interfaceBoardDTO.setMac(mac);
        interfaceBoardDTO.setReceptionOrder(receptionOrder);

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
    public List<InterfaceBoardDTO> getInterfaceBoardAssignedByContract(String reference) throws ControlTxException {
        List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByReference(
            reference
        );

        return controlInterfaceBoardList.stream().map(ControlInterfaceBoardDTO::getInterfaceBoard).toList();
    }

    @Override
    public Long getCountInterfaceBoardByContracted(String reference) throws ControlTxException {
        List<ContractDTO> contractList = contractService.getContractByReference(reference);

        return contractList.stream().mapToLong(ContractDTO::getAmountInterfaceBoard).sum();
    }
}
