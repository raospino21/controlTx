package co.com.ies.smol.domain.core;

import co.com.ies.smol.domain.core.error.ControlTxException;
import co.com.ies.smol.service.dto.ContractDTO;
import co.com.ies.smol.service.dto.ControlInterfaceBoardDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import co.com.ies.smol.service.dto.PurchaseOrderDTO;
import co.com.ies.smol.service.dto.core.RequestStatusRecord;
import co.com.ies.smol.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ControlTxDomainImpl {

    public ControlInterfaceBoardDTO validateExistingBoardControl(Optional<ControlInterfaceBoardDTO> oControlInterfaceBoardDTO)
        throws ControlTxException {
        if (oControlInterfaceBoardDTO.isEmpty()) {
            throw new ControlTxException(ControlTxException.BOARD_NOT_FOUND);
        }
        ControlInterfaceBoardDTO controlBoardExists = oControlInterfaceBoardDTO.get();
        if ("OPERATION".equals(controlBoardExists.getState().name())) {
            throw new ControlTxException(ControlTxException.BOARD_ASSIGNED);
        }
        return controlBoardExists;
    }

    public InterfaceBoardDTO validateExistingInterfaceBoard(Optional<InterfaceBoardDTO> oInterfaceBoardDTO) throws ControlTxException {
        if (oInterfaceBoardDTO.isEmpty()) {
            throw new ControlTxException(ControlTxException.BOARD_NOT_FOUND);
        }
        return oInterfaceBoardDTO.get();
    }

    public ContractDTO validateExistingContract(Optional<ContractDTO> oContract) throws ControlTxException {
        if (oContract.isEmpty()) {
            throw new ControlTxException(ControlTxException.CONTRACT_NOT_FOUND);
        }
        return oContract.get();
    }

    public PurchaseOrderDTO validateExistingPurchaseOrderAndGet(Optional<PurchaseOrderDTO> oPurchaseOrderDTO) throws ControlTxException {
        if (oPurchaseOrderDTO.isEmpty()) {
            throw new ControlTxException(ControlTxException.PURCHASE_ORDER_NOT_FOUND);
        }

        return oPurchaseOrderDTO.get();
    }

    public void validateAvailability(Long boardOrderIes, Long boardHypotheticalTotal) throws ControlTxException {
        if (boardHypotheticalTotal > boardOrderIes) {
            throw new ControlTxException("El monto a recibir supera al total de la orden compra (" + boardHypotheticalTotal + ")");
        }
    }

    public void validateIncomingBoardSize(Long amountReceived, Long boardSize) throws ControlTxException {
        if (!Objects.equals(amountReceived, boardSize)) {
            throw new ControlTxException(ControlTxException.AMOUNT_OF_BOARDS_EXCEEDS_THE_RECEIVED);
        }
    }

    public RequestStatusRecord buildResponse(List<InterfaceBoardDTO> existingInterfaces) {
        if (existingInterfaces.isEmpty()) {
            return new RequestStatusRecord("CreateBoardRegister", "Proceso Exitoso!!", 200);
        }
        String macWithErrors = existingInterfaces.stream().map(InterfaceBoardDTO::getMac).collect(Collectors.joining(", "));

        return new RequestStatusRecord("Error al registrar tarjeta", macWithErrors, 400);
    }

    public OperatorDTO validateExistingOperator(Optional<OperatorDTO> oOperatorDto) throws ControlTxException {
        if (oOperatorDto.isEmpty()) {
            throw new ControlTxException(ControlTxException.OPERATOR_NOT_FOUND);
        }
        return oOperatorDto.get();
    }

    public void isItPossibleToAssociate(Long amountReceptionOrder, int alreadyLinked, Long amountReceived) throws ControlTxException {
        Long available = amountReceptionOrder - alreadyLinked;
        if (amountReceived > available) {
            throw new ControlTxException("El monto a recibir  excedido - disponible " + available);
        }
    }

    public boolean isAvailable(Long amountReceptionOrder, int alreadyLinked) {
        return (amountReceptionOrder - alreadyLinked) > 0;
    }
}
