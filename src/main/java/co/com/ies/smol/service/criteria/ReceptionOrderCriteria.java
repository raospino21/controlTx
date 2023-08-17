package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.ReceptionOrder} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.ReceptionOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reception-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceptionOrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter providerLotNumber;

    private LongFilter amountReceived;

    private StringFilter remission;

    private ZonedDateTimeFilter entryDate;

    private ZonedDateTimeFilter warrantyDate;

    private LongFilter purchaseOrderId;

    private Boolean distinct;

    public ReceptionOrderCriteria() {}

    public ReceptionOrderCriteria(ReceptionOrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.providerLotNumber = other.providerLotNumber == null ? null : other.providerLotNumber.copy();
        this.amountReceived = other.amountReceived == null ? null : other.amountReceived.copy();
        this.remission = other.remission == null ? null : other.remission.copy();
        this.entryDate = other.entryDate == null ? null : other.entryDate.copy();
        this.warrantyDate = other.warrantyDate == null ? null : other.warrantyDate.copy();
        this.purchaseOrderId = other.purchaseOrderId == null ? null : other.purchaseOrderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReceptionOrderCriteria copy() {
        return new ReceptionOrderCriteria(this);
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

    public LongFilter getProviderLotNumber() {
        return providerLotNumber;
    }

    public LongFilter providerLotNumber() {
        if (providerLotNumber == null) {
            providerLotNumber = new LongFilter();
        }
        return providerLotNumber;
    }

    public void setProviderLotNumber(LongFilter providerLotNumber) {
        this.providerLotNumber = providerLotNumber;
    }

    public LongFilter getAmountReceived() {
        return amountReceived;
    }

    public LongFilter amountReceived() {
        if (amountReceived == null) {
            amountReceived = new LongFilter();
        }
        return amountReceived;
    }

    public void setAmountReceived(LongFilter amountReceived) {
        this.amountReceived = amountReceived;
    }

    public StringFilter getRemission() {
        return remission;
    }

    public StringFilter remission() {
        if (remission == null) {
            remission = new StringFilter();
        }
        return remission;
    }

    public void setRemission(StringFilter remission) {
        this.remission = remission;
    }

    public ZonedDateTimeFilter getEntryDate() {
        return entryDate;
    }

    public ZonedDateTimeFilter entryDate() {
        if (entryDate == null) {
            entryDate = new ZonedDateTimeFilter();
        }
        return entryDate;
    }

    public void setEntryDate(ZonedDateTimeFilter entryDate) {
        this.entryDate = entryDate;
    }

    public ZonedDateTimeFilter getWarrantyDate() {
        return warrantyDate;
    }

    public ZonedDateTimeFilter warrantyDate() {
        if (warrantyDate == null) {
            warrantyDate = new ZonedDateTimeFilter();
        }
        return warrantyDate;
    }

    public void setWarrantyDate(ZonedDateTimeFilter warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public LongFilter getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public LongFilter purchaseOrderId() {
        if (purchaseOrderId == null) {
            purchaseOrderId = new LongFilter();
        }
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(LongFilter purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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
        final ReceptionOrderCriteria that = (ReceptionOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(providerLotNumber, that.providerLotNumber) &&
            Objects.equals(amountReceived, that.amountReceived) &&
            Objects.equals(remission, that.remission) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(warrantyDate, that.warrantyDate) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, providerLotNumber, amountReceived, remission, entryDate, warrantyDate, purchaseOrderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceptionOrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (providerLotNumber != null ? "providerLotNumber=" + providerLotNumber + ", " : "") +
            (amountReceived != null ? "amountReceived=" + amountReceived + ", " : "") +
            (remission != null ? "remission=" + remission + ", " : "") +
            (entryDate != null ? "entryDate=" + entryDate + ", " : "") +
            (warrantyDate != null ? "warrantyDate=" + warrantyDate + ", " : "") +
            (purchaseOrderId != null ? "purchaseOrderId=" + purchaseOrderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
