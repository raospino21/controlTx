package co.com.ies.smol.service.criteria;

import co.com.ies.smol.domain.enumeration.ContractType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.Contract} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.ContractResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contracts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContractType
     */
    public static class ContractTypeFilter extends Filter<ContractType> {

        public ContractTypeFilter() {}

        public ContractTypeFilter(ContractTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContractTypeFilter copy() {
            return new ContractTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reference;

    private ContractTypeFilter type;

    private LongFilter amountInterfaceBoard;

    private ZonedDateTimeFilter startTime;

    private ZonedDateTimeFilter finishTime;

    private LongFilter operatorId;

    private Boolean distinct;

    public ContractCriteria() {}

    public ContractCriteria(ContractCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.amountInterfaceBoard = other.amountInterfaceBoard == null ? null : other.amountInterfaceBoard.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.finishTime = other.finishTime == null ? null : other.finishTime.copy();
        this.operatorId = other.operatorId == null ? null : other.operatorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContractCriteria copy() {
        return new ContractCriteria(this);
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

    public StringFilter getReference() {
        return reference;
    }

    public StringFilter reference() {
        if (reference == null) {
            reference = new StringFilter();
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public ContractTypeFilter getType() {
        return type;
    }

    public ContractTypeFilter type() {
        if (type == null) {
            type = new ContractTypeFilter();
        }
        return type;
    }

    public void setType(ContractTypeFilter type) {
        this.type = type;
    }

    public LongFilter getAmountInterfaceBoard() {
        return amountInterfaceBoard;
    }

    public LongFilter amountInterfaceBoard() {
        if (amountInterfaceBoard == null) {
            amountInterfaceBoard = new LongFilter();
        }
        return amountInterfaceBoard;
    }

    public void setAmountInterfaceBoard(LongFilter amountInterfaceBoard) {
        this.amountInterfaceBoard = amountInterfaceBoard;
    }

    public ZonedDateTimeFilter getStartTime() {
        return startTime;
    }

    public ZonedDateTimeFilter startTime() {
        if (startTime == null) {
            startTime = new ZonedDateTimeFilter();
        }
        return startTime;
    }

    public void setStartTime(ZonedDateTimeFilter startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTimeFilter getFinishTime() {
        return finishTime;
    }

    public ZonedDateTimeFilter finishTime() {
        if (finishTime == null) {
            finishTime = new ZonedDateTimeFilter();
        }
        return finishTime;
    }

    public void setFinishTime(ZonedDateTimeFilter finishTime) {
        this.finishTime = finishTime;
    }

    public LongFilter getOperatorId() {
        return operatorId;
    }

    public LongFilter operatorId() {
        if (operatorId == null) {
            operatorId = new LongFilter();
        }
        return operatorId;
    }

    public void setOperatorId(LongFilter operatorId) {
        this.operatorId = operatorId;
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
        final ContractCriteria that = (ContractCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(type, that.type) &&
            Objects.equals(amountInterfaceBoard, that.amountInterfaceBoard) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(finishTime, that.finishTime) &&
            Objects.equals(operatorId, that.operatorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, type, amountInterfaceBoard, startTime, finishTime, operatorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (amountInterfaceBoard != null ? "amountInterfaceBoard=" + amountInterfaceBoard + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (finishTime != null ? "finishTime=" + finishTime + ", " : "") +
            (operatorId != null ? "operatorId=" + operatorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
