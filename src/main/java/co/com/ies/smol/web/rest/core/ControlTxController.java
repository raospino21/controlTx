package co.com.ies.smol.web.rest.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ControlTxController {

    private final Logger log = LoggerFactory.getLogger(ControlTxController.class);

    private final ControlTxService controlTxService;

    public ControlTxController(ControlTxService controlTxService) {
        this.controlTxService = controlTxService;
    }

    @PostMapping("/board/register")
    public ResponseEntity<String> createBoardRegister(@Valid @RequestBody BoardRegisterDTO boardRegisterDTO, Errors errors)
        throws URISyntaxException {
        log.debug("REST request to save BoardRegisterDTO : {}", boardRegisterDTO);

        FieldError fieldError = errors.getFieldError();

        if (errors.hasErrors() && Objects.nonNull(fieldError)) {
            String errorMsg = fieldError.getField().concat(" ").concat(fieldError.getDefaultMessage());
            return ResponseEntity.ok(errorMsg);
        }

        controlTxService.createBoardRegister(boardRegisterDTO);

        return ResponseEntity.ok("ok process succesfully!!");
    }

    @PostMapping("/assign/board")
    public ResponseEntity<String> assignInterfaceBoard(@Valid @RequestBody AssignBoardDTO assignBoardDTO, Errors errors)
        throws ControlTxException {
        log.debug("REST request to save assignInterfaceBoard : {}", assignBoardDTO);

        FieldError fieldError = errors.getFieldError();

        if (errors.hasErrors() && Objects.nonNull(fieldError)) {
            String errorMsg = fieldError.getField().concat(" ").concat(fieldError.getDefaultMessage());
            return ResponseEntity.ok(errorMsg);
        }

        controlTxService.assignInterfaceBoard(assignBoardDTO);

        return ResponseEntity.ok("ok process assignInterfaceBoard succesfully!!");
    }

    /**
     *
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
     * Entrega las tarjetas que está asociadas al contrato del operador
     */
    @GetMapping("/interface-boards/assigned-by-operator/{reference}")
    public ResponseEntity<List<InterfaceBoardDTO>> getInterfaceBoardContratedByOperator(@PathVariable String reference)
        throws ControlTxException {
        log.debug("REST request getInterfaceBoardByOperator reference : {}", reference);

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
}
