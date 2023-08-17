package co.com.ies.smol.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReceptionOrder.
 */
@Entity
@Table(name = "reception_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceptionOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "provider_lot_number", nullable = false)
    private Long providerLotNumber;

    @NotNull
    @Column(name = "amount_received", nullable = false)
    private Long amountReceived;

    @NotNull
    @Size(max = 30)
    @Column(name = "remission", length = 30, nullable = false)
    private String remission;

    @NotNull
    @Column(name = "entry_date", nullable = false)
    private ZonedDateTime entryDate;

    @NotNull
    @Column(name = "warranty_date", nullable = false)
    private ZonedDateTime warrantyDate;

    @ManyToOne(optional = false)
    @NotNull
    private PurchaseOrder purchaseOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReceptionOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProviderLotNumber() {
        return this.providerLotNumber;
    }

    public ReceptionOrder providerLotNumber(Long providerLotNumber) {
        this.setProviderLotNumber(providerLotNumber);
        return this;
    }

    public void setProviderLotNumber(Long providerLotNumber) {
        this.providerLotNumber = providerLotNumber;
    }

    public Long getAmountReceived() {
        return this.amountReceived;
    }

    public ReceptionOrder amountReceived(Long amountReceived) {
        this.setAmountReceived(amountReceived);
        return this;
    }

    public void setAmountReceived(Long amountReceived) {
        this.amountReceived = amountReceived;
    }

    public String getRemission() {
        return this.remission;
    }

    public ReceptionOrder remission(String remission) {
        this.setRemission(remission);
        return this;
    }

    public void setRemission(String remission) {
        this.remission = remission;
    }

    public ZonedDateTime getEntryDate() {
        return this.entryDate;
    }

    public ReceptionOrder entryDate(ZonedDateTime entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(ZonedDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public ZonedDateTime getWarrantyDate() {
        return this.warrantyDate;
    }

    public ReceptionOrder warrantyDate(ZonedDateTime warrantyDate) {
        this.setWarrantyDate(warrantyDate);
        return this;
    }

    public void setWarrantyDate(ZonedDateTime warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public ReceptionOrder purchaseOrder(PurchaseOrder purchaseOrder) {
        this.setPurchaseOrder(purchaseOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceptionOrder)) {
            return false;
        }
        return id != null && id.equals(((ReceptionOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceptionOrder{" +
            "id=" + getId() +
            ", providerLotNumber=" + getProviderLotNumber() +
            ", amountReceived=" + getAmountReceived() +
            ", remission='" + getRemission() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", warrantyDate='" + getWarrantyDate() + "'" +
            "}";
    }
}
