package co.com.ies.smol.web.rest.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.criteria.OperatorCriteria;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.dto.core.*;
import co.com.ies.smol.service.dto.core.sub.ContractSubDTO;
import co.com.ies.smol.web.rest.errors.BadRequestAlertException;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class ControlTxController {

    private final Logger log = LoggerFactory.getLogger(ControlTxController.class);

    private final ControlTxService controlTxService;

    private static final String ENTITY_NAME = "ControlTx";

    public ControlTxController(ControlTxService controlTxService) {
        this.controlTxService = controlTxService;
    }

    @PostMapping("/board/register")
    public ResponseEntity<RequestStatusRecord> createBoardRegister(@Valid @RequestBody BoardRegisterDTO boardRegisterDTO, Errors errors)
        throws ControlTxException {
        log.debug("REST request to save BoardRegisterDTO : {}", boardRegisterDTO);

        FieldError fieldError = errors.getFieldError();

        if (errors.hasErrors() && Objects.nonNull(fieldError)) {
            String errorMsg = fieldError.getField().concat(" ").concat(fieldError.getDefaultMessage());
            throw new BadRequestAlertException(errorMsg, ENTITY_NAME, "400");
        }

        return ResponseEntity.ok(controlTxService.createBoardRegister(boardRegisterDTO));
    }

    @GetMapping("/assign/board")
    public ResponseEntity<Resource> assignInterfaceBoard(
        @RequestParam(value = "reference", required = false) String reference,
        @RequestParam(value = "contractType", required = false) ContractType contractType,
        @RequestParam(value = "amountToAssociate", required = false) int amountToAssociate
    ) throws ControlTxException {
        log.debug(
            "REST request to save assignInterfaceBoard amountToAssociate: {} : reference {} : contractType {}",
            amountToAssociate,
            reference,
            contractType
        );

        final ByteArrayInputStream fileInMemory = controlTxService.assignInterfaceBoard(amountToAssociate, reference, contractType);
        final InputStreamResource fileInputStream = new InputStreamResource(fileInMemory);

        final String nameFile = "Tarjetas asignadas " + reference;

        final HttpHeaders headers = new HttpHeaders();
        headers.set("filename", nameFile);
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
    }

    /**
     * Entrega las tarjetas que fueron contratadas de todos los operadores asociados a la marca
     * tener en cuenta que solo para contratos vigentes
     */
    @GetMapping("/interface-boards/assigned-operator-by-brand/{brandName}")
    public ResponseEntity<List<InterfaceBoardDTO>> getInterfaceBoardByBrand(@PathVariable String brandName) {
        log.debug("REST request getInterfaceBoardByBrand brandName : {}", brandName);

        return ResponseEntity.ok(controlTxService.getInterfaceBoardByBrand(brandName));
    }

    /**
     * Entrega la cantidad de tarjetas que fueron contratadas de todos los operadores asociados a la marca
     * tener en cuenta que solo para contratos vigentes
     */
    @GetMapping("/count/interface-boards/by-brand-contrated/{brandName}")
    public ResponseEntity<Long> getCountInterfaceBoardByBrand(@PathVariable String brandName) {
        log.debug("REST request getCountInterfaceBoardByBrand brandName : {}", brandName);

        return ResponseEntity.ok(controlTxService.getCountInterfaceBoardByBrand(brandName));
    }

    /**
     * Entrega la cantidad de tarjetas estan vinculadas a todos los operadores asociados a la marca
     * tener en cuenta que solo para contratos vigentes
     */

    @GetMapping("/count/interface-boards/assigned-operator-by-brand/{brandName}")
    public ResponseEntity<Integer> getCountInterfaceBoardByBrandAssignedOperator(@PathVariable String brandName) {
        log.debug("REST request getInterfaceBoardByBrand brandName : {}", brandName);

        return ResponseEntity.ok(controlTxService.getInterfaceBoardByBrand(brandName).size());
    }

    /**
     * Entrega las tarjetas que está asociadas a un operador según el tipo de contrato
     */
    @GetMapping("/interface-boards/assigned-by-operator/{reference}/{contractType}")
    public ResponseEntity<List<InterfaceBoardDTO>> getInterfaceBoardContratedByOperatorAndContractType(
        @PathVariable String reference,
        @PathVariable ContractType contractType,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws ControlTxException {
        log.debug(
            "REST request getInterfaceBoardContratedByOperatorAndContractType reference : {} - contractType {}",
            reference,
            contractType
        );

        Page<InterfaceBoardDTO> page = controlTxService.getInterfaceBoardAssignedByContractAndType(reference, contractType, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Entrega las tarjetas que está asociadas a un operador sin importar el tipo de contrato
     */
    @GetMapping("/interface-boards/assigned-by-operator/{reference}")
    public ResponseEntity<List<InterfaceBoardDTO>> getInterfaceBoardContratedByOperator(@PathVariable String reference)
        throws ControlTxException {
        log.debug("REST request getInterfaceBoardByOperator reference : {} ", reference);

        return ResponseEntity.ok(controlTxService.getInterfaceBoardAssignedByContract(reference));
    }

    /**
     * Entrega la cantdad de tarjetas que fueron contratadas
     */
    @GetMapping("/count/interface-boards/contracted/{reference}")
    public ResponseEntity<Long> getCountInterfaceBoardByContracted(@PathVariable String reference) throws ControlTxException {
        log.debug("REST request getCountInterfaceBoardByContract reference : {}", reference);

        return ResponseEntity.ok(controlTxService.getCountInterfaceBoardByContracted(reference));
    }

    /**
     * Entrega la cantdad de tarjetas que fueron contratadas egún el tipo de contrato
     */
    @GetMapping("/count/interface-boards/contracted/{reference}/{contractType}")
    public ResponseEntity<Long> getCountInterfaceBoardByContractedAndType(
        @PathVariable String reference,
        @PathVariable ContractType contractType
    ) throws ControlTxException {
        log.debug("REST request getCountInterfaceBoardByContractedAndType reference : {}, contractType {}", reference, contractType);

        return ResponseEntity.ok(controlTxService.getCountInterfaceBoardByContractedAndType(reference, contractType));
    }

    /**
     * Entrega la información de tarjetas asociadas vs contratadas
     */
    @GetMapping("/info/boards/association/{operatorId}")
    public ResponseEntity<BoardAssociationResponseDTO> getInfoBoardAssociation(@PathVariable Long operatorId) throws ControlTxException {
        log.debug("REST request getInfoBoardAssociation operatorId : {} ", operatorId);

        return ResponseEntity.ok(controlTxService.getInfoBoardAssociation(operatorId));
    }

    /****
     * Entrega la información de tarjetas asociadas vs contratadas
     */
    @GetMapping("/info/boards/association-by-referece/{reference}")
    public ResponseEntity<List<ContractSubDTO>> getInfoBoardAssociationByReference(@PathVariable String reference)
        throws ControlTxException {
        log.debug("REST request getInfoBoardAssociation operatorId : {} ", reference);

        return ResponseEntity.ok(controlTxService.getInfoBoardAssociationByReference(reference));
    }

    /**
     * Entrega la tarjetas disponibles en stock
     * contiene filtro y es paginado
     */
    @GetMapping("/info/boards/available")
    public ResponseEntity<List<InterfaceBoardDTO>> getInfoBoardsAvailable(
        @RequestParam(value = "mac", required = false) String mac,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws ControlTxException {
        log.debug("REST request getInfoBoardsAvailable mac {}", mac);

        Page<InterfaceBoardDTO> page = controlTxService.getInfoBoardsAvailable(mac, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Entrega la cantidad disponibles en stock
     */
    @GetMapping("/count/boards/available")
    public ResponseEntity<Integer> getCountBoardsAvailable() throws ControlTxException {
        log.debug("REST request getCountBoardsAvailable ");

        return ResponseEntity.ok(controlTxService.getCountBoardsAvailable());
    }

    /**
     * Entrega la tarjetas de un operador por estdado
     */
    @GetMapping("/info/boards/by-state/{operatorId}/{state}")
    public ResponseEntity<List<InterfaceBoardDTO>> getInfoBoardsByOperatorIdAndState(
        @PathVariable Long operatorId,
        @PathVariable StatusInterfaceBoard state
    ) {
        log.debug("REST request getInfoBoardsAvailable");

        return ResponseEntity.ok(controlTxService.getInfoBoardsByOperatorIdAndState(operatorId, state));
    }

    /**
     * Entrega los registro de control interface board vinculadas a contratos
     */
    @GetMapping("/info/control-interface-boards/linked/")
    public ResponseEntity<List<ControlInterfaceBoardDTO>> getControlInterfaceBoardLinked(
        @RequestParam(value = "mac", required = false) String mac,
        @RequestParam(value = "reference", required = false) String reference,
        @RequestParam(value = "operator", required = false) String operatorName,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws ControlTxException {
        FilterControlInterfaceBoard filter = new FilterControlInterfaceBoard(mac, reference, operatorName);
        log.debug("REST request getControlInterfaceBoardAvailable filter {}", filter);

        Page<ControlInterfaceBoardDTO> page = controlTxService.getControlInterfaceBoardAvailable(filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Entrega los contratos pendientes por asociar tarjetas
     */
    @GetMapping("/info/pending/contracts/boards")
    public ResponseEntity<List<ContractDTO>> getPendingContractsForBoard() {
        log.debug("REST request getPendingContractsForBoard  ");

        return ResponseEntity.ok(controlTxService.getPendingContractsForBoard());
    }

    /**
     * Entrega las ordenes de recepción pendientes por asociar tarjetas
     */
    @GetMapping("/controltx/info/reception-order")
    public ResponseEntity<List<ReceptionOrderDTO>> getPendingReceptionOrderForBoard() {
        log.debug("REST request getPendingReceptionOrderForBoard  ");
        return ResponseEntity.ok(controlTxService.getPendingReceptionOrderForBoard());
    }

    /**
     * Entrega las ordenes de compras pendientes por asociar una orden de recepción
     */
    @GetMapping("/info/purchase-order")
    public ResponseEntity<List<PurchaseOrderDTO>> getPendingPurchaseOrderForReceptionOrder() {
        log.debug("REST request getPendingPurchaseOrderForReceptionOrder  ");
        return ResponseEntity.ok(controlTxService.getPendingPurchaseOrderForReceptionOrder());
    }

    @PostMapping("/controltx/reception-orders")
    public ResponseEntity<ReceptionOrderDTO> createReceptionOrder(@Valid @RequestBody ReceptionOrderDTO receptionOrderDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReceptionOrder : {}", receptionOrderDTO);
        if (receptionOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new receptionOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceptionOrderDTO result;
        try {
            result = controlTxService.saveReceptionOrder(receptionOrderDTO);
        } catch (ControlTxException e) {
            throw new BadRequestAlertException("limit exceeded", ENTITY_NAME, e.getMessage());
        }
        return ResponseEntity
            .created(new URI("/api/reception-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/purchase-orders/complete")
    public ResponseEntity<List<PurchaseOrderCompleteResponse>> getAllPurchaseOrdersComplete(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get getAllPurchaseOrdersComplete by pag: {}", pageable);

        Page<PurchaseOrderCompleteResponse> page = controlTxService.getAllPurchaseOrdersComplete(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/brand/complete/info")
    public ResponseEntity<List<BrandCompleteInfoResponse>> getCompleteInfoBrands(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get getCompleteInfoBrands by pag: {}", pageable);

        Page<BrandCompleteInfoResponse> page = controlTxService.getCompleteInfoBrands(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/operator/complete/info")
    public ResponseEntity<List<OperatorCompleteInfoResponse>> getCompleteInfoOperators(
        OperatorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get getCompleteInfoOperators by pag: {}", pageable);

        Page<OperatorCompleteInfoResponse> page = controlTxService.getCompleteInfoOperators(criteria, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/download/file/operator-boards/{contractId}")
    public ResponseEntity<Resource> downloadOperatorBoards(@PathVariable Long contractId) {
        log.info("REST request to downloadOperatorBoards by contractId {}", contractId);

        final ByteArrayInputStream fileInMemory = controlTxService.getFileWithOperatorBoardsByContractId(contractId);
        final InputStreamResource fileInputStream = new InputStreamResource(fileInMemory);

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8");

        return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
    }

    @GetMapping("/board/details-in-sotck")
    public ResponseEntity<BoardDetailsInSotckRecord> getBoardDetailsInSotck() {
        log.debug("REST request togetBoardDetailsInSotck");

        return ResponseEntity.ok(controlTxService.getBoardDetailsInSotck());
    }

    @GetMapping("/detail/reception-order/{receptionOrderId}")
    public ResponseEntity<OrderReceptionDetailRecord> getDetailReceptionOrder(@PathVariable Long receptionOrderId) {
        log.debug("REST request togetBoardDetailsInSotck");

        return ResponseEntity.ok(controlTxService.getDetailReceptionOrder(receptionOrderId));
    }

    @GetMapping("/download/file/boards-in-sotck/")
    public ResponseEntity<Resource> downloadBoardsInStock() {
        log.info("REST request to downloadBoardsInStock");

        final ByteArrayInputStream fileInMemory = controlTxService.getFileTheBoardsInStock();
        final InputStreamResource fileInputStream = new InputStreamResource(fileInMemory);

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8");

        return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
    }

    @GetMapping("/download/file/associated-boards/reception-order/{validated}/{receptionOrderId}")
    public ResponseEntity<Resource> downloadAssociatedBoardsReceptionOrder(
        @PathVariable Boolean validated,
        @PathVariable Long receptionOrderId
    ) {
        log.info("REST request to downloadBoardsInStock");

        final ByteArrayInputStream fileInMemory = controlTxService.getFileBoardsAssociated(validated, receptionOrderId);
        final InputStreamResource fileInputStream = new InputStreamResource(fileInMemory);

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8");

        return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
    }

    @GetMapping("/purchase-order/general/detail")
    public ResponseEntity<OrderPurchaseGeneralDetail> getGeneralDetailPurchaseOrder() {
        log.debug("REST request getGeneralDetailPurchaseOrder");

        return ResponseEntity.ok(controlTxService.getGeneralDetailPurchaseOrder());
    }

    @GetMapping("/boards-by-brand/general/detail")
    public ResponseEntity<BoardsByBrandGeneralDetail> getBoardsByBrandGeneralDetail() {
        log.debug("REST request getBoardsByBrandGeneralDetail");

        return ResponseEntity.ok(controlTxService.getBoardsByBrandGeneralDetail());
    }
}
