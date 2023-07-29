package co.com.ies.smol.service.criteria;

import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link co.com.ies.smol.domain.ControlInterfaceBoard} entity. This class is used
 * in {@link co.com.ies.smol.web.rest.ControlInterfaceBoardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /control-interface-boards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ControlInterfaceBoardCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Location
     */
    public static class LocationFilter extends Filter<Location> {

        public LocationFilter() {}

        public LocationFilter(LocationFilter filter) {
            super(filter);
        }

        @Override
        public LocationFilter copy() {
            return new LocationFilter(this);
        }
    }

    /**
     * Class for filtering StatusInterfaceBoard
     */
    public static class StatusInterfaceBoardFilter extends Filter<StatusInterfaceBoard> {

        public StatusInterfaceBoardFilter() {}

        public StatusInterfaceBoardFilter(StatusInterfaceBoardFilter filter) {
            super(filter);
        }

        @Override
        public StatusInterfaceBoardFilter copy() {
            return new StatusInterfaceBoardFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocationFilter location;

    private StatusInterfaceBoardFilter state;

    private ZonedDateTimeFilter startTime;

    private ZonedDateTimeFilter finishTime;

    private LongFilter contractId;

    private LongFilter interfaceBoardId;

    private Boolean distinct;

    public ControlInterfaceBoardCriteria() {}

    public ControlInterfaceBoardCriteria(ControlInterfaceBoardCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.finishTime = other.finishTime == null ? null : other.finishTime.copy();
        this.contractId = other.contractId == null ? null : other.contractId.copy();
        this.interfaceBoardId = other.interfaceBoardId == null ? null : other.interfaceBoardId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ControlInterfaceBoardCriteria copy() {
        return new ControlInterfaceBoardCriteria(this);
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

    public LocationFilter getLocation() {
        return location;
    }

    public LocationFilter location() {
        if (location == null) {
            location = new LocationFilter();
        }
        return location;
    }

    public void setLocation(LocationFilter location) {
        this.location = location;
    }

    public StatusInterfaceBoardFilter getState() {
        return state;
    }

    public StatusInterfaceBoardFilter state() {
        if (state == null) {
            state = new StatusInterfaceBoardFilter();
        }
        return state;
    }

    public void setState(StatusInterfaceBoardFilter state) {
        this.state = state;
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

    public LongFilter getContractId() {
        return contractId;
    }

    public LongFilter contractId() {
        if (contractId == null) {
            contractId = new LongFilter();
        }
        return contractId;
    }

    public void setContractId(LongFilter contractId) {
        this.contractId = contractId;
    }

    public LongFilter getInterfaceBoardId() {
        return interfaceBoardId;
    }

    public LongFilter interfaceBoardId() {
        if (interfaceBoardId == null) {
            interfaceBoardId = new LongFilter();
        }
        return interfaceBoardId;
    }

    public void setInterfaceBoardId(LongFilter interfaceBoardId) {
        this.interfaceBoardId = interfaceBoardId;
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
        final ControlInterfaceBoardCriteria that = (ControlInterfaceBoardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(location, that.location) &&
            Objects.equals(state, that.state) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(finishTime, that.finishTime) &&
            Objects.equals(contractId, that.contractId) &&
            Objects.equals(interfaceBoardId, that.interfaceBoardId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, state, startTime, finishTime, contractId, interfaceBoardId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControlInterfaceBoardCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (finishTime != null ? "finishTime=" + finishTime + ", " : "") +
            (contractId != null ? "contractId=" + contractId + ", " : "") +
            (interfaceBoardId != null ? "interfaceBoardId=" + interfaceBoardId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
