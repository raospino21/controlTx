package co.com.ies.smol.web.rest.core;

import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import java.net.URISyntaxException;
import java.util.Objects;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
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
        throws URISyntaxException {
        log.debug("REST request to save assignInterfaceBoard : {}", assignBoardDTO);

        FieldError fieldError = errors.getFieldError();

        if (errors.hasErrors() && Objects.nonNull(fieldError)) {
            String errorMsg = fieldError.getField().concat(" ").concat(fieldError.getDefaultMessage());
            return ResponseEntity.ok(errorMsg);
        }

        controlTxService.assignInterfaceBoard(assignBoardDTO);

        return ResponseEntity.ok("ok process assignInterfaceBoard succesfully!!");
    }
}
