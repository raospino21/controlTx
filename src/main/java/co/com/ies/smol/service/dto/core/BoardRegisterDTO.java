package co.com.ies.smol.service.dto.core;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class BoardRegisterDTO implements Serializable {

    @NotNull
    private Long colcircuitosLotNumber;

    @NotNull
    private Long amountReceived;

    @NotNull
    @Size(max = 30)
    private String remission;

    @NotNull
    private Long iesOrderNumber;

    @NotNull
    private List<String> macs;

    public Long getColcircuitosLotNumber() {
        return colcircuitosLotNumber;
    }

    public void setColcircuitosLotNumber(Long colcircuitosLotNumber) {
        this.colcircuitosLotNumber = colcircuitosLotNumber;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardRegisterDTO that = (BoardRegisterDTO) o;
        return (
            Objects.equals(colcircuitosLotNumber, that.colcircuitosLotNumber) &&
            Objects.equals(amountReceived, that.amountReceived) &&
            Objects.equals(remission, that.remission) &&
            Objects.equals(iesOrderNumber, that.iesOrderNumber) &&
            Objects.equals(macs, that.macs)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(colcircuitosLotNumber, amountReceived, remission, iesOrderNumber, macs);
    }

    @Override
    public String toString() {
        return (
            "BoardRegisterDTO [colcircuitosLotNumber=" +
            colcircuitosLotNumber +
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
