package co.com.ies.smol.service.core.impl;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.domain.core.ControlTxDomainImpl;
import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.BrandService;
import co.com.ies.smol.service.ContractService;
import co.com.ies.smol.service.ControlInterfaceBoardService;
import co.com.ies.smol.service.InterfaceBoardService;
import co.com.ies.smol.service.OperatorService;
import co.com.ies.smol.service.PurchaseOrderService;
import co.com.ies.smol.service.ReceptionOrderService;
import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.criteria.ControlInterfaceBoardCriteria;
import co.com.ies.smol.service.criteria.OperatorCriteria;
import co.com.ies.smol.service.dto.BrandDTO;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardAssociationResponseDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import co.com.ies.smol.service.dto.core.BrandCompleteInfoResponse;
import co.com.ies.smol.service.dto.core.FilterControlInterfaceBoard;
import co.com.ies.smol.service.dto.core.InfoBoardByFileRecord;
import co.com.ies.smol.service.dto.core.OperatorCompleteInfoResponse;
import co.com.ies.smol.service.dto.core.PurchaseOrderCompleteResponse;
import co.com.ies.smol.service.dto.core.RequestStatusRecord;
import co.com.ies.smol.service.dto.core.sub.ContractSubDTO;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
            createInterfaceBoard(mac, receptionOrderDTO);
        }

        return buildResponse(existingInterfaces);
    }

    protected InterfaceBoardDTO createInterfaceBoard(String mac, ReceptionOrderDTO receptionOrder) {
        InterfaceBoardDTO interfaceBoardDTO = new InterfaceBoardDTO();
        interfaceBoardDTO.setMac(mac);
        interfaceBoardDTO.setIsValidated(false);
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
        List<ContractSubDTO> contractSubList = getContractSubListByOperatorId(operatorId);

        return new BoardAssociationResponseDTO(contractSubList);
    }

    protected List<ContractSubDTO> getContractSubListByOperatorId(Long operatorId) {
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
                contract.getType(),
                contract.getReference(),
                contract.getId()
            );
            contractSubList.add(contractSub);
        });
        return contractSubList;
    }

    @Override
    public Page<InterfaceBoardDTO> getInfoBoardsAvailable(String mac, @org.springdoc.api.annotations.ParameterObject Pageable pageable)
        throws ControlTxException {
        if (Objects.nonNull(mac)) {
            final InterfaceBoardDTO interfaceBoard = interfaceBoardService
                .getInterfaceBoardByMac(mac)
                .orElseThrow(() -> new ControlTxException("Tarjeta no encontrada " + mac));

            Long interfaceBoardId = interfaceBoard.getId();
            Page<InterfaceBoardDTO> interfaceBoardPage = controlInterfaceBoardService
                .getInfoBoardsAvailableByinterfaceBoardId(interfaceBoardId, pageable)
                .map(ControlInterfaceBoardDTO::getInterfaceBoard);

            if (interfaceBoardPage.getContent().isEmpty()) {
                throw new ControlTxException("Tarjeta " + mac + " no se encuentra en stock");
            }
            return interfaceBoardPage;
        }

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

    @Override
    public Page<OperatorCompleteInfoResponse> getCompleteInfoOperators(OperatorCriteria criteria, Pageable pageable) {
        Page<OperatorDTO> operatorsPage = operatorService.findByCriteria(criteria, pageable);
        List<OperatorDTO> operatorList = operatorsPage.getContent();
        List<OperatorCompleteInfoResponse> response = new ArrayList<>();

        operatorList.forEach(operator -> {
            Long operatorId = operator.getId();
            List<ContractSubDTO> contractSubList = getContractSubListByOperatorId(operatorId);
            response.add(new OperatorCompleteInfoResponse(operator, contractSubList));
        });

        return new PageImpl<>(response, pageable, operatorsPage.getTotalElements());
    }

    @Override
    public ByteArrayInputStream getFileWithOperatorBoardsByContractId(Long contractId) {
        List<ControlInterfaceBoardDTO> controlInterfaceBoardList = controlInterfaceBoardService.getControlInterfaceBoardByContractId(
            contractId
        );

        final CsvMapper mapper = new CsvMapper();
        final CsvSchema schema = mapper.schemaFor(InfoBoardByFileRecord.class);
        schema.withColumnSeparator(';');
        schema.usesHeader();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        controlInterfaceBoardList.forEach(data -> {
            String operatorName = data.getContract().getOperator().getName();
            String reference = data.getContract().getReference();
            ContractType type = data.getContract().getType();
            String mac = data.getInterfaceBoard().getMac();
            InfoBoardByFileRecord dataFile = new InfoBoardByFileRecord(operatorName, reference, type, mac);

            try {
                mapper.writer(schema).writeValue(out, dataFile);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });

        return new ByteArrayInputStream(out.toByteArray());
    }
}
