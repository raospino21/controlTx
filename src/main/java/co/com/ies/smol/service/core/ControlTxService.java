package co.com.ies.smol.service.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.service.dto.core.AssignBoardDTO;
import co.com.ies.smol.service.dto.core.BoardRegisterDTO;

public interface ControlTxService {
    void createBoardRegister(BoardRegisterDTO boardRegisterDTO);

    void assignInterfaceBoard(AssignBoardDTO assignBoardDTO) throws ControlTxException;
}
