package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.service.dto.ReceptionOrderDTO;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class BoardRegisterDTO implements Serializable {

    @NotNull
    private ReceptionOrderDTO receptionOrder;

    @NotNull
    private List<String> macs;

    public void setMacs(List<String> macs) {
        this.macs = macs;
    }

    public List<String> getMacs() {
        return macs;
    }

    public ReceptionOrderDTO getReceptionOrder() {
        return receptionOrder;
    }

    public void setReceptionOrder(ReceptionOrderDTO receptionOrder) {
        this.receptionOrder = receptionOrder;
    }

    @Override
    public String toString() {
        return "BoardRegisterDTO [receptionOrder=" + receptionOrder + ", macs=" + macs + "]";
    }
}
