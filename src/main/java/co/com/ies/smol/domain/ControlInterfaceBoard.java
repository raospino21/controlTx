package co.com.ies.smol.domain;

import co.com.ies.smol.domain.enumeration.Location;
import co.com.ies.smol.domain.enumeration.StatusInterfaceBoard;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ControlInterfaceBoard.
 */
@Entity
@Table(name = "control_interface_board")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ControlInterfaceBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "controlInterfaceBoardIdSeq")
    @SequenceGenerator(name = "controlInterfaceBoardIdSeq")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    private Location location;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private StatusInterfaceBoard state;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "finish_time")
    private ZonedDateTime finishTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "operator" }, allowSetters = true)
    private Contract contract;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "receptionOrder" }, allowSetters = true)
    private InterfaceBoard interfaceBoard;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ControlInterfaceBoard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getLocation() {
        return this.location;
    }

    public ControlInterfaceBoard location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public StatusInterfaceBoard getState() {
        return this.state;
    }

    public ControlInterfaceBoard state(StatusInterfaceBoard state) {
        this.setState(state);
        return this;
    }

    public void setState(StatusInterfaceBoard state) {
        this.state = state;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public ControlInterfaceBoard startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getFinishTime() {
        return this.finishTime;
    }

    public ControlInterfaceBoard finishTime(ZonedDateTime finishTime) {
        this.setFinishTime(finishTime);
        return this;
    }

    public void setFinishTime(ZonedDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public ControlInterfaceBoard contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public InterfaceBoard getInterfaceBoard() {
        return this.interfaceBoard;
    }

    public void setInterfaceBoard(InterfaceBoard interfaceBoard) {
        this.interfaceBoard = interfaceBoard;
    }

    public ControlInterfaceBoard interfaceBoard(InterfaceBoard interfaceBoard) {
        this.setInterfaceBoard(interfaceBoard);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ControlInterfaceBoard)) {
            return false;
        }
        return id != null && id.equals(((ControlInterfaceBoard) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControlInterfaceBoard{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", state='" + getState() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", finishTime='" + getFinishTime() + "'" +
            "}";
    }
}
