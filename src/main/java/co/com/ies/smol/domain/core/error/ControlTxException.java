package co.com.ies.smol.domain.core.error;

public class ControlTxException extends Exception {

    public static final String BOARD_NOT_FOUND = "Board not found";
    public static final String BOARD_IS_NOT_IN_STOCK = "Board is not in stock";
    public static final String BOARD_ASSIGNED = "Board assigned";
    public static final String OPERATOR_NOT_FOUND = "Operator not found";
    public static final String OPERATOR_HAS_NOT_CONTRACTED = "Operator has not contracted";
    public static final String CONTRACT_NOT_FOUND = "Contract not found";

    public ControlTxException() {
        super("Error - ControlTxException ");
    }

    public ControlTxException(String msg) {
        super(msg);
    }
}
