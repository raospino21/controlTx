package co.com.ies.smol.domain.core.error;

public class ControlTxException extends Exception {

    public static final String BOARD_NOT_FOUND = "Tarjeta no encontrada";
    public static final String BOARD_IS_NOT_IN_STOCK = "Tarjeta no esta en Stock";
    public static final String BOARD_ASSIGNED = "Tarjeta asignada";
    public static final String OPERATOR_NOT_FOUND = "Operador no encontrado";
    public static final String OPERATOR_HAS_NOT_CONTRACTED = "Operador no tiene contrato";
    public static final String CONTRACT_NOT_FOUND = "Contrato no encontrado";
    public static final String PURCHASE_ORDER_NOT_FOUND = "Orden de compra no encontrada";
    public static final String AMOUNT_OF_BOARDS_EXCEEDS_THE_RECEIVED =
        "El monto de la tarjetas a recibir no coincide con la cantidad de tarjetas";

    public ControlTxException() {
        super("Error - ControlTxException ");
    }

    public ControlTxException(String msg) {
        super(msg);
    }
}
