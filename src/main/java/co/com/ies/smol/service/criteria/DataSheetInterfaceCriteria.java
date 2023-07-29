package co.com.ies.smol.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.DataSheetInterface} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.DataSheetInterfaceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /data-sheet-interfaces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DataSheetInterfaceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter colcircuitosLotNumber;

    private LongFilter orderAmount;

    private LongFilter amountReceived;

    private StringFilter remission;

    private ZonedDateTimeFilter entryDate;

    private LongFilter iesOrderNumber;

    private Boolean distinct;

    public DataSheetInterfaceCriteria() {}

    public DataSheetInterfaceCriteria(DataSheetInterfaceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.colcircuitosLotNumber = other.colcircuitosLotNumber == null ? null : other.colcircuitosLotNumber.copy();
        this.orderAmount = other.orderAmount == null ? null : other.orderAmount.copy();
        this.amountReceived = other.amountReceived == null ? null : other.amountReceived.copy();
        this.remission = other.remission == null ? null : other.remission.copy();
        this.entryDate = other.entryDate == null ? null : other.entryDate.copy();
        this.iesOrderNumber = other.iesOrderNumber == null ? null : other.iesOrderNumber.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DataSheetInterfaceCriteria copy() {
        return new DataSheetInterfaceCriteria(this);
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

    public LongFilter getColcircuitosLotNumber() {
        return colcircuitosLotNumber;
    }

    public LongFilter colcircuitosLotNumber() {
        if (colcircuitosLotNumber == null) {
            colcircuitosLotNumber = new LongFilter();
        }
        return colcircuitosLotNumber;
    }

    public void setColcircuitosLotNumber(LongFilter colcircuitosLotNumber) {
        this.colcircuitosLotNumber = colcircuitosLotNumber;
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
        final DataSheetInterfaceCriteria that = (DataSheetInterfaceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(colcircuitosLotNumber, that.colcircuitosLotNumber) &&
            Objects.equals(orderAmount, that.orderAmount) &&
            Objects.equals(amountReceived, that.amountReceived) &&
            Objects.equals(remission, that.remission) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(iesOrderNumber, that.iesOrderNumber) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, colcircuitosLotNumber, orderAmount, amountReceived, remission, entryDate, iesOrderNumber, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataSheetInterfaceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (colcircuitosLotNumber != null ? "colcircuitosLotNumber=" + colcircuitosLotNumber + ", " : "") +
            (orderAmount != null ? "orderAmount=" + orderAmount + ", " : "") +
            (amountReceived != null ? "amountReceived=" + amountReceived + ", " : "") +
            (remission != null ? "remission=" + remission + ", " : "") +
            (entryDate != null ? "entryDate=" + entryDate + ", " : "") +
            (iesOrderNumber != null ? "iesOrderNumber=" + iesOrderNumber + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
