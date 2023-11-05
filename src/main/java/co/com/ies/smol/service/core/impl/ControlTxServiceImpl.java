package co.com.ies.smol.service.core.impl;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.domain.core.ControlTxDomainImpl;
import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.*;
import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.criteria.ControlInterfaceBoardCriteria;
import co.com.ies.smol.service.criteria.ControlInterfaceBoardCriteria.StatusInterfaceBoardFilter;
import co.com.ies.smol.service.dto.*;
import co.com.ies.smol.service.dto.core.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

@Transactional
@Service
public class ControlTxServiceImpl extends ControlTxDomainImpl implements ControlTxService {

    private final Logger log = LoggerFactory.getLogger(ControlTxServiceImpl.class);

    private final ReceptionOrderService receptionOrderService;
    private final InterfaceBoardService interfaceBoardService;
    private final ControlInterfaceBoardService controlInterfaceBoardService;
    private final ContractService contractService;
    private final OperatorService operatorService;
    private final BrandService brandService;
    private final PurchaseOrderService purchaseOrderService;

    public ControlTxServiceImpl(
        ReceptionOrderService receptionOrderService,
        InterfaceBoardService interfaceBoardService,
        ControlInterfaceBoardService controlInterfaceBoardService,
        ContractService contractService,
        OperatorService operatorService,
        PurchaseOrderService purchaseOrderService,
        BrandService brandService
    ) {
        this.receptionOrderService = receptionOrderService;
        this.interfaceBoardService = interfaceBoardService;
        this.controlInterfaceBoardService = controlInterfaceBoardService;
        this.contractService = contractService;
        this.operatorService = operatorService;
        this.purchaseOrderService = purchaseOrderService;
        this.brandService = brandService;
    }

    @Override
    public RequestStatusRecord createBoardRegister(BoardRegisterDTO boardRegisterDTO) throws ControlTxException {
        List<String> macs = boardRegisterDTO.getMacs();
        ReceptionOrderDTO receptionOrderDTO = boardRegisterDTO.getReceptionOrder();

        List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByReceptionOrderIdAndFinishTimeIsNull(
            receptionOrderDTO.getId()
        );
        isItPossibleToAssociate(receptionOrderDTO.getAmountReceived(), controlInterfaceBoardList.size(), Long.valueOf(macs.size()));

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

        return buildResponse(existingInterfaces);
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
    public Page<InterfaceBoardDTO> getInfoBoardsAvailable(String mac, @org.springdoc.api.annotations.ParameterObject Pageable pageable)
        throws ControlTxException {
        ControlInterfaceBoardCriteria criteria = new ControlInterfaceBoardCriteria();

        if (Objects.nonNull(mac)) {
            Optional<InterfaceBoardDTO> oInterfaceBoard = interfaceBoardService.getInterfaceBoardByMac(mac);

            if (oInterfaceBoard.isEmpty()) {
                throw new ControlTxException("Tarjeta no encontrada " + mac);
            }
            LongFilter interfaceBoardFilter = new LongFilter();
            interfaceBoardFilter.setEquals(oInterfaceBoard.get().getId());
            criteria.setInterfaceBoardId(interfaceBoardFilter);
        }

        StatusInterfaceBoardFilter statusFilter = new StatusInterfaceBoardFilter();
        statusFilter.setEquals(StatusInterfaceBoard.STOCK);
        criteria.setState(statusFilter);

        ZonedDateTimeFilter finishTimeFilter = new ZonedDateTimeFilter();
        finishTimeFilter.setEquals(null);
        criteria.setFinishTime(finishTimeFilter);

        return controlInterfaceBoardService.getInfoBoardsAvailable(criteria, pageable).map(ControlInterfaceBoardDTO::getInterfaceBoard);
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
    public Page<ControlInterfaceBoardDTO> getControlInterfaceBoardAvailable(FilterControlInterfaceBoard filter, Pageable pageable)
        throws ControlTxException {
        ControlInterfaceBoardCriteria criteria = new ControlInterfaceBoardCriteria();

        LongFilter contractFilter = new LongFilter();
        contractFilter.setNotEquals(null);
        criteria.setContractId(contractFilter);

        LongFilter interfaceBoardFilter = new LongFilter();
        interfaceBoardFilter.setNotEquals(null);
        criteria.setInterfaceBoardId(interfaceBoardFilter);

        if (Objects.nonNull(filter.reference())) {
            List<ContractDTO> contractList = contractService.getContractByReference(filter.reference());

            if (contractList.isEmpty()) {
                throw new ControlTxException("Contrato no encontrado (" + filter.reference() + ")");
            }
            List<Long> contractIdList = contractList.stream().map(ContractDTO::getId).toList();
            contractFilter.setIn(contractIdList);
            criteria.setContractId(contractFilter);
        }

        if (Objects.nonNull(filter.mac())) {
            final InterfaceBoardDTO interfaceBoard = interfaceBoardService
                .getInterfaceBoardByMac(filter.mac())
                .orElseThrow(() -> new ControlTxException("Tarjeta no encontrada (" + filter.mac() + ")"));

            interfaceBoardFilter.setEquals(interfaceBoard.getId());
            criteria.setInterfaceBoardId(interfaceBoardFilter);
        }

        return controlInterfaceBoardService.getControlInterfaceBoardAvailable(criteria, pageable);
    }

    @Override
    public List<ContractDTO> getPendingContractsForBoard() {
        List<ContractDTO> contractList = contractService.findAll();

        List<ContractDTO> pendingContractsForBoard = new ArrayList<>();

        contractList.forEach(contract -> {
            List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByContractId(
                contract.getId()
            );

            if ((contract.getAmountInterfaceBoard() - controlInterfaceBoardList.size()) != 0) {
                pendingContractsForBoard.add(contract);
            }
        });

        return pendingContractsForBoard;
    }

    @Override
    public List<ReceptionOrderDTO> getPendingReceptionOrderForBoard() {
        List<ReceptionOrderDTO> receptionOrderList = receptionOrderService.getAllReceptionOrder();
        List<ReceptionOrderDTO> receptionOrderListAvailable = new ArrayList<>();

        receptionOrderList.forEach(receptionOrder -> {
            List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByReceptionOrderIdAndFinishTimeIsNull(
                receptionOrder.getId()
            );
            boolean isAvailable = isAvailable(receptionOrder.getAmountReceived(), controlInterfaceBoardList.size());

            if (isAvailable) {
                receptionOrderListAvailable.add(receptionOrder);
            }
        });

        return receptionOrderListAvailable;
    }

    @Override
    public List<PurchaseOrderDTO> getPendingPurchaseOrderForReceptionOrder() {
        List<PurchaseOrderDTO> purchaseOrderList = purchaseOrderService.getAllPurchaseOrder();
        List<PurchaseOrderDTO> purchaseOrderrListAvailable = new ArrayList<>();

        List<ReceptionOrderDTO> receptionOrderList = receptionOrderService.getAllReceptionOrder();

        purchaseOrderList.forEach(purchaseOrder -> {
            Long totalAmount = receptionOrderList
                .stream()
                .filter(receptionOrder -> receptionOrder.getPurchaseOrder().getId().equals(purchaseOrder.getId()))
                .mapToLong(ReceptionOrderDTO::getAmountReceived)
                .sum();
            boolean isAvailable = isAvailable(purchaseOrder.getOrderAmount(), totalAmount.intValue());

            if (isAvailable) {
                purchaseOrderrListAvailable.add(purchaseOrder);
            }
        });

        return purchaseOrderrListAvailable;
    }

    @Override
    public ReceptionOrderDTO saveReceptionOrder(ReceptionOrderDTO receptionOrderDTO) throws ControlTxException {
        PurchaseOrderDTO purchaseOrderDTO = receptionOrderDTO.getPurchaseOrder();
        Long purchaseOrderId = purchaseOrderDTO.getIesOrderNumber();

        List<ReceptionOrderDTO> receptionOrderList = receptionOrderService.getReceptionOrderByIesOrderNumber(purchaseOrderId);

        Long boardReceived = receptionOrderList.stream().mapToLong(ReceptionOrderDTO::getAmountReceived).sum();
        Long boardToRegister = receptionOrderDTO.getAmountReceived();

        Long boardHypotheticalTotal = boardReceived + boardToRegister;
        Long boardOrderIes = purchaseOrderDTO.getOrderAmount();

        validateAvailability(boardOrderIes, boardHypotheticalTotal);

        return receptionOrderService.save(receptionOrderDTO);
    }

    @Override
    public Page<PurchaseOrderCompleteResponse> getAllPurchaseOrdersComplete(Pageable pageable) {
        Page<PurchaseOrderDTO> purchaseOrderPage = purchaseOrderService.findAll(pageable);
        List<PurchaseOrderDTO> purchaseOrderList = purchaseOrderPage.getContent();
        List<PurchaseOrderCompleteResponse> response = new ArrayList<>();

        purchaseOrderList.forEach(purchaseOrder -> {
            List<ReceptionOrderDTO> receptionOrderList = receptionOrderService.getReceptionOrderByPurchaseOrderId(purchaseOrder.getId());
            response.add(new PurchaseOrderCompleteResponse(purchaseOrder, receptionOrderList));
        });

        return new PageImpl<>(response, pageable, purchaseOrderPage.getTotalElements());
    }

    @Override
    public Page<BrandCompleteInfoResponse> getCompleteInfoBrands(Pageable pageable) {
        Page<BrandDTO> brandsPage = brandService.findAll(pageable);
        List<BrandDTO> brandList = brandsPage.getContent();
        List<BrandCompleteInfoResponse> response = new ArrayList<>();

        brandList.forEach(brand -> {
            Long totalAmountBoardcontracted = getCountInterfaceBoardByBrand(brand.getName());
            Long totalAmountBoardAssigned = Long.valueOf(getInterfaceBoardByBrand(brand.getName()).size());

            response.add(new BrandCompleteInfoResponse(brand, totalAmountBoardcontracted, totalAmountBoardAssigned));
        });

        return new PageImpl<>(response, pageable, brandsPage.getTotalElements());
    }
}
