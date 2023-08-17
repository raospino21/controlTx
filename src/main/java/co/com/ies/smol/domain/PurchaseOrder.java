package co.com.ies.smol.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PurchaseOrder.
 */
@Entity
@Table(name = "purchase_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_amount", nullable = false)
    private Long orderAmount;

    @NotNull
    @Column(name = "create_at", nullable = false)
    private ZonedDateTime createAt;

    @NotNull
    @Column(name = "ies_order_number", nullable = false)
    private Long iesOrderNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderAmount() {
        return this.orderAmount;
    }

    public PurchaseOrder orderAmount(Long orderAmount) {
        this.setOrderAmount(orderAmount);
        return this;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public PurchaseOrder createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public Long getIesOrderNumber() {
        return this.iesOrderNumber;
    }

    public PurchaseOrder iesOrderNumber(Long iesOrderNumber) {
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
        if (!(o instanceof PurchaseOrder)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrder{" +
            "id=" + getId() +
            ", orderAmount=" + getOrderAmount() +
            ", createAt='" + getCreateAt() + "'" +
            ", iesOrderNumber=" + getIesOrderNumber() +
            "}";
    }
}
