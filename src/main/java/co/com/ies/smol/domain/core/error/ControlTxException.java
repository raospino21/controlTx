package co.com.ies.smol.domain.core.error;

public class ControlTxException extends Exception {

    public static final String BOARD_NOT_FOUND = "Board not found";
    public static final String BOARD_IS_NOT_IN_STOCK = "Board is not in stock";

    public ControlTxException() {
        super("Error - ControlTxException ");
    }

    public ControlTxException(String msg) {
        super(msg);
    }
}
