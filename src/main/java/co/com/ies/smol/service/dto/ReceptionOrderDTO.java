package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.ReceptionOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceptionOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private Long providerLotNumber;

    @NotNull
    private Long amountReceived;

    @NotNull
    @Size(max = 30)
    private String remission;

    @NotNull
    private ZonedDateTime entryDate;

    @NotNull
    private ZonedDateTime warrantyDate;

    private PurchaseOrderDTO purchaseOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProviderLotNumber() {
        return providerLotNumber;
    }

    public void setProviderLotNumber(Long providerLotNumber) {
        this.providerLotNumber = providerLotNumber;
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

    public ZonedDateTime getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(ZonedDateTime warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public PurchaseOrderDTO getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceptionOrderDTO)) {
            return false;
        }

        ReceptionOrderDTO receptionOrderDTO = (ReceptionOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, receptionOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceptionOrderDTO{" +
            "id=" + getId() +
            ", providerLotNumber=" + getProviderLotNumber() +
            ", amountReceived=" + getAmountReceived() +
            ", remission='" + getRemission() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", warrantyDate='" + getWarrantyDate() + "'" +
            ", purchaseOrder=" + getPurchaseOrder() +
            "}";
    }
}
