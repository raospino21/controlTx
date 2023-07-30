package co.com.ies.smol.web.rest.core;

import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> createDTO(@RequestBody BoardRegisterDTO boardRegisterDTO) throws URISyntaxException {
        log.debug("REST request to save BoardRegisterDTO : {}", boardRegisterDTO);

        controlTxService.createBoardRegister(boardRegisterDTO);

        return ResponseEntity.ok("ok process succesfully!!");
    }
}
