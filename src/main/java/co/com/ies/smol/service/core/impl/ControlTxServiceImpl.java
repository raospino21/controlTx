package co.com.ies.smol.service.core.impl;

import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import co.com.ies.smol.service.ControlInterfaceBoardService;
import co.com.ies.smol.service.DataSheetInterfaceService;
import co.com.ies.smol.service.InterfaceBoardService;
import co.com.ies.smol.service.core.ControlTxService;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.DataSheetInterfaceDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;
import co.com.ies.smol.web.rest.core.ControlTxController;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ControlTxServiceImpl implements ControlTxService {

    private final Logger log = LoggerFactory.getLogger(ControlTxController.class);

    private final DataSheetInterfaceService dataSheetInterfaceService;
    private final InterfaceBoardService interfaceBoardService;
    private final ControlInterfaceBoardService controlInterfaceBoardService;

    public ControlTxServiceImpl(
        DataSheetInterfaceService dataSheetInterfaceService,
        ControlInterfaceBoardService controlInterfaceBoardService,
        InterfaceBoardService interfaceBoardService
    ) {
        this.dataSheetInterfaceService = dataSheetInterfaceService;
        this.controlInterfaceBoardService = controlInterfaceBoardService;
        this.interfaceBoardService = interfaceBoardService;
    }

    @Override
    public void createBoardRegister(BoardRegisterDTO boardRegisterDTO) {
        List<String> macs = boardRegisterDTO.getMacs();
        DataSheetInterfaceDTO dataSheetInterfaceDTO = createDataSheetInterface(boardRegisterDTO);
        dataSheetInterfaceDTO = dataSheetInterfaceService.save(dataSheetInterfaceDTO);

        for (String mac : macs) {
            InterfaceBoardDTO interfaceBoardDTO = createInterfaceBoard(mac, dataSheetInterfaceDTO);
            interfaceBoardDTO = interfaceBoardService.save(interfaceBoardDTO);

            ControlInterfaceBoardDTO controlInterfaceBoardDTO = createControlInterfaceBoard(interfaceBoardDTO);
            controlInterfaceBoardDTO = controlInterfaceBoardService.save(controlInterfaceBoardDTO);

            log.info(
                "dataSheetInterfaceDTO ID {}  - interfaceBoardDTO ID {} - controlInterfaceBoardDTO ID {} ",
                dataSheetInterfaceDTO.getId(),
                interfaceBoardDTO.getId(),
                controlInterfaceBoardDTO.getId()
            );
        }
    }

    protected DataSheetInterfaceDTO createDataSheetInterface(BoardRegisterDTO boardRegisterDTO) {
        DataSheetInterfaceDTO dataSheetInterfaceDTO = new DataSheetInterfaceDTO();
        dataSheetInterfaceDTO.setColcircuitosLotNumber(boardRegisterDTO.getColcircuitosLotNumber());
        dataSheetInterfaceDTO.setOrderAmount(boardRegisterDTO.getOrderAmount());
        dataSheetInterfaceDTO.setAmountReceived(boardRegisterDTO.getAmountReceived());
        dataSheetInterfaceDTO.setRemission(boardRegisterDTO.getRemission());
        dataSheetInterfaceDTO.setEntryDate(ZonedDateTime.now());
        dataSheetInterfaceDTO.setIesOrderNumber(boardRegisterDTO.getIesOrderNumber());

        return dataSheetInterfaceDTO;
    }

    protected InterfaceBoardDTO createInterfaceBoard(String mac, DataSheetInterfaceDTO dataSheetInterfaceDTO) {
        InterfaceBoardDTO interfaceBoardDTO = new InterfaceBoardDTO();
        interfaceBoardDTO.setMac(mac);
        interfaceBoardDTO.setDataSheetInterface(dataSheetInterfaceDTO);

        return interfaceBoardDTO;
    }

    protected ControlInterfaceBoardDTO createControlInterfaceBoard(InterfaceBoardDTO interfaceBoardDTO) {
        ControlInterfaceBoardDTO controlInterfaceBoardDTO = new ControlInterfaceBoardDTO();
        controlInterfaceBoardDTO.setLocation(Location.IES);
        controlInterfaceBoardDTO.setState(StatusInterfaceBoard.STOCK);
        controlInterfaceBoardDTO.setStartTime(ZonedDateTime.now());
        controlInterfaceBoardDTO.setInterfaceBoard(interfaceBoardDTO);

        return controlInterfaceBoardDTO;
    }
}
