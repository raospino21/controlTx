package co.com.ies.smol.service.dto.core;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class BoardRegisterDTO implements Serializable {

    @NotNull
    private Long providerLotNumber;

    @NotNull
    private Long amountReceived;

    @NotNull
    @Size(max = 30)
    private String remission;

    @NotNull
    private Long iesOrderNumber;

    @NotNull
    private List<String> macs;

    public Long getProviderLotNumber() {
        return providerLotNumber;
    }

    public void setProviderLotNumber(Long providerLotNumber) {
        this.providerLotNumber = providerLotNumber;
    }

    public void setMacs(List<String> macs) {
        this.macs = macs;
    }

    public Long getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(Long amountReceived) {
        this.amountReceived = amountReceived;
    }

    public String getRemission() {
        return remission;
    }

    public void setRemission(String remission) {
        this.remission = remission;
    }

    public Long getIesOrderNumber() {
        return iesOrderNumber;
    }

    public void setIesOrderNumber(Long iesOrderNumber) {
        this.iesOrderNumber = iesOrderNumber;
    }

    public List<String> getMacs() {
        return macs;
    }

    public void setMac(List<String> macs) {
        this.macs = macs;
    }

    @Override
    public String toString() {
        return (
            "BoardRegisterDTO [providerLotNumber=" +
            providerLotNumber +
            ", amountReceived=" +
            amountReceived +
            ", remission=" +
            remission +
            ", iesOrderNumber=" +
            iesOrderNumber +
            ", macs=" +
            macs +
            "]"
        );
    }
}
