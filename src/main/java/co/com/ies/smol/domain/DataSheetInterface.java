package co.com.ies.smol.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DataSheetInterface.
 */
@Entity
@Table(name = "data_sheet_interface")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DataSheetInterface implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "colcircuitos_lot_number", nullable = false)
    private Long colcircuitosLotNumber;

    @NotNull
    @Column(name = "order_amount", nullable = false)
    private Long orderAmount;

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
    @Column(name = "ies_order_number", nullable = false)
    private Long iesOrderNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DataSheetInterface id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getColcircuitosLotNumber() {
        return this.colcircuitosLotNumber;
    }

    public DataSheetInterface colcircuitosLotNumber(Long colcircuitosLotNumber) {
        this.setColcircuitosLotNumber(colcircuitosLotNumber);
        return this;
    }

    public void setColcircuitosLotNumber(Long colcircuitosLotNumber) {
        this.colcircuitosLotNumber = colcircuitosLotNumber;
    }

    public Long getOrderAmount() {
        return this.orderAmount;
    }

    public DataSheetInterface orderAmount(Long orderAmount) {
        this.setOrderAmount(orderAmount);
        return this;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getAmountReceived() {
        return this.amountReceived;
    }

    public DataSheetInterface amountReceived(Long amountReceived) {
        this.setAmountReceived(amountReceived);
        return this;
    }

    public void setAmountReceived(Long amountReceived) {
        this.amountReceived = amountReceived;
    }

    public String getRemission() {
        return this.remission;
    }

    public DataSheetInterface remission(String remission) {
        this.setRemission(remission);
        return this;
    }

    public void setRemission(String remission) {
        this.remission = remission;
    }

    public ZonedDateTime getEntryDate() {
        return this.entryDate;
    }

    public DataSheetInterface entryDate(ZonedDateTime entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(ZonedDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public Long getIesOrderNumber() {
        return this.iesOrderNumber;
    }

    public DataSheetInterface iesOrderNumber(Long iesOrderNumber) {
        this.setIesOrderNumber(iesOrderNumber);
        return this;
    }

    public void setIesOrderNumber(Long iesOrderNumber) {
        this.iesOrderNumber = iesOrderNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSheetInterface)) {
            return false;
        }
        return id != null && id.equals(((DataSheetInterface) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataSheetInterface{" +
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
