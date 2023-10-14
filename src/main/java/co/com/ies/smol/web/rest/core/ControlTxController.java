package co.com.ies.smol.web.rest.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.domain.enumeration.ContractType;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardAssociationResponseDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import co.com.ies.smol.service.dto.core.FilterControlInterfaceBoard;
import co.com.ies.smol.service.dto.core.RequestStatusRecord;
import co.com.ies.smol.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
        String message = controlTxService.createBoardRegister(boardRegisterDTO);

        return ResponseEntity.ok(new RequestStatusRecord(message, 200));
    }

    @PostMapping("/assign/board")
    public ResponseEntity<RequestStatusRecord> assignInterfaceBoard(@Valid @RequestBody AssignBoardDTO assignBoardDTO, Errors errors)
        throws ControlTxException {
        log.debug("REST request to save assignInterfaceBoard : {}", assignBoardDTO);

        FieldError fieldError = errors.getFieldError();

        if (errors.hasErrors() && Objects.nonNull(fieldError)) {
            String errorMsg = fieldError.getField().concat(" ").concat(fieldError.getDefaultMessage());
            throw new BadRequestAlertException(errorMsg, ENTITY_NAME, "400");
        }

        controlTxService.assignInterfaceBoard(assignBoardDTO);

        return ResponseEntity.ok(new RequestStatusRecord("ok process assignInterfaceBoard succesfully!!", 200));
    }

    /**
     * Entrega las tarjetas que fueron contratadas de todos los operadores asociados a la marca
     * tener en cuenta que solo para contratos vigentes
     */
    @GetMapping("/interface-boards/assigned-operator-by-brand/{brandName}")
    public ResponseEntity<List<InterfaceBoardDTO>> getInterfaceBoardByBrand(@PathVariable String brandName) throws ControlTxException {
        log.debug("REST request getInterfaceBoardByBrand brandName : {}", brandName);

        return ResponseEntity.ok(controlTxService.getInterfaceBoardByBrand(brandName));
    }

    /**
     * Entrega la cantidad de tarjetas que fueron contratadas de todos los operadores asociados a la marca
     * tener en cuenta que solo para contratos vigentes
     */
    @GetMapping("/count/interface-boards/by-brand-contrated/{brandName}")
    public ResponseEntity<Long> getCountInterfaceBoardByBrand(@PathVariable String brandName) {
        log.debug("REST request getInterfaceBoardByBrand brandName : {}", brandName);

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
        @PathVariable ContractType contractType
    ) throws ControlTxException {
        log.debug(
            "REST request getInterfaceBoardContratedByOperatorAndContractType reference : {} - contractType {}",
            reference,
            contractType
        );

        return ResponseEntity.ok(controlTxService.getInterfaceBoardAssignedByContractAndType(reference, contractType));
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

    /**
     * Entrega la tarjetas disponibles en stock
     */
    @GetMapping("/info/boards/available")
    public ResponseEntity<List<InterfaceBoardDTO>> getInfoBoardsAvailable(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request getInfoBoardsAvailable");

        Page<InterfaceBoardDTO> page = controlTxService.getInfoBoardsAvailable(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Entrega la tarjetas disponibles en stock
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
     * Entrega los registro de control interface board disponibles
     */
    @GetMapping("/info/control-interface-boards/avalaible/")
    public ResponseEntity<List<ControlInterfaceBoardDTO>> getControlInterfaceBoardAvailable(
        @RequestParam(value = "mac", required = false) String mac,
        @RequestParam(value = "reference", required = false) String reference,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        FilterControlInterfaceBoard filter = new FilterControlInterfaceBoard(mac, reference);
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
     * Entrega los contratos pendientes por asociar tarjetas
     */
    @GetMapping("/info/receptionOrder")
    public ResponseEntity<List<ReceptionOrderDTO>> getPendingReceptionOrderForBoard() {
        log.debug("REST request getPendingReceptionOrderForBoard  ");
        return ResponseEntity.ok(controlTxService.getPendingReceptionOrderForBoard());
    }
}
