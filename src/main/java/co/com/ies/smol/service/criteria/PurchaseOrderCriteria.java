package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.PurchaseOrder} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.PurchaseOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /purchase-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseOrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter orderAmount;

    private ZonedDateTimeFilter createAt;

    private LongFilter iesOrderNumber;

    private Boolean distinct;

    public PurchaseOrderCriteria() {}

    public PurchaseOrderCriteria(PurchaseOrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderAmount = other.orderAmount == null ? null : other.orderAmount.copy();
        this.createAt = other.createAt == null ? null : other.createAt.copy();
        this.iesOrderNumber = other.iesOrderNumber == null ? null : other.iesOrderNumber.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PurchaseOrderCriteria copy() {
        return new PurchaseOrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getOrderAmount() {
        return orderAmount;
    }

    public LongFilter orderAmount() {
        if (orderAmount == null) {
            orderAmount = new LongFilter();
        }
        return orderAmount;
    }

    public void setOrderAmount(LongFilter orderAmount) {
        this.orderAmount = orderAmount;
    }

    public ZonedDateTimeFilter getCreateAt() {
        return createAt;
    }

    public ZonedDateTimeFilter createAt() {
        if (createAt == null) {
            createAt = new ZonedDateTimeFilter();
        }
        return createAt;
    }

    public void setCreateAt(ZonedDateTimeFilter createAt) {
        this.createAt = createAt;
    }

    public LongFilter getIesOrderNumber() {
        return iesOrderNumber;
    }

    public LongFilter iesOrderNumber() {
        if (iesOrderNumber == null) {
            iesOrderNumber = new LongFilter();
        }
        return iesOrderNumber;
    }

    public void setIesOrderNumber(LongFilter iesOrderNumber) {
        this.iesOrderNumber = iesOrderNumber;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PurchaseOrderCriteria that = (PurchaseOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderAmount, that.orderAmount) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(iesOrderNumber, that.iesOrderNumber) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderAmount, createAt, iesOrderNumber, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (orderAmount != null ? "orderAmount=" + orderAmount + ", " : "") +
            (createAt != null ? "createAt=" + createAt + ", " : "") +
            (iesOrderNumber != null ? "iesOrderNumber=" + iesOrderNumber + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
