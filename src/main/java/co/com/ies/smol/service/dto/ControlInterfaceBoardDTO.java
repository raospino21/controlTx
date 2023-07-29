package co.com.ies.smol.service.dto;

import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.ControlInterfaceBoard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ControlInterfaceBoardDTO implements Serializable {

    private Long id;

    @NotNull
    private Location location;

    @NotNull
    private StatusInterfaceBoard state;

    @NotNull
    private ZonedDateTime startTime;

    private ZonedDateTime finishTime;

    private ContractDTO contract;

    private InterfaceBoardDTO interfaceBoard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public StatusInterfaceBoard getState() {
        return state;
    }

    public void setState(StatusInterfaceBoard state) {
        this.state = state;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(ZonedDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
    }

    public InterfaceBoardDTO getInterfaceBoard() {
        return interfaceBoard;
    }

    public void setInterfaceBoard(InterfaceBoardDTO interfaceBoard) {
        this.interfaceBoard = interfaceBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ControlInterfaceBoardDTO)) {
            return false;
        }

        ControlInterfaceBoardDTO controlInterfaceBoardDTO = (ControlInterfaceBoardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, controlInterfaceBoardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControlInterfaceBoardDTO{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", state='" + getState() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", finishTime='" + getFinishTime() + "'" +
            ", contract=" + getContract() +
            ", interfaceBoard=" + getInterfaceBoard() +
            "}";
    }
}
