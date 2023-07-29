package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.DataSheetInterface} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DataSheetInterfaceDTO implements Serializable {

    private Long id;

    @NotNull
    private Long colcircuitosLotNumber;

    @NotNull
    private Long orderAmount;

    @NotNull
    private Long amountReceived;

    @NotNull
    @Size(max = 30)
    private String remission;

    @NotNull
    private ZonedDateTime entryDate;

    @NotNull
    private Long iesOrderNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getColcircuitosLotNumber() {
        return colcircuitosLotNumber;
    }

    public void setColcircuitosLotNumber(Long colcircuitosLotNumber) {
        this.colcircuitosLotNumber = colcircuitosLotNumber;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
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

    public ZonedDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(ZonedDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public Long getIesOrderNumber() {
        return iesOrderNumber;
    }

    public void setIesOrderNumber(Long iesOrderNumber) {
        this.iesOrderNumber = iesOrderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSheetInterfaceDTO)) {
            return false;
        }

        DataSheetInterfaceDTO dataSheetInterfaceDTO = (DataSheetInterfaceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dataSheetInterfaceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataSheetInterfaceDTO{" +
            "id=" + getId() +
            ", colcircuitosLotNumber=" + getColcircuitosLotNumber() +
            ", orderAmount=" + getOrderAmount() +
            ", amountReceived=" + getAmountReceived() +
            ", remission='" + getRemission() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", iesOrderNumber=" + getIesOrderNumber() +
            "}";
    }
}
