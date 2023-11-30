package co.com.ies.smol.domain;

import co.com.ies.smol.domain.enumeration.ContractType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contractIdSeq")
    @SequenceGenerator(name = "contractIdSeq")
    @Column(name = "id")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ContractType type;

    @NotNull
    @Column(name = "amount_interface_board", nullable = false)
    private Long amountInterfaceBoard;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "finish_time")
    private ZonedDateTime finishTime;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "brand" }, allowSetters = true)
    private Operator operator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public Contract reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ContractType getType() {
        return this.type;
    }

    public Contract type(ContractType type) {
        this.setType(type);
        return this;
    }

    public void setType(ContractType type) {
        this.type = type;
    }

    public Long getAmountInterfaceBoard() {
        return this.amountInterfaceBoard;
    }

    public Contract amountInterfaceBoard(Long amountInterfaceBoard) {
        this.setAmountInterfaceBoard(amountInterfaceBoard);
        return this;
    }

    public void setAmountInterfaceBoard(Long amountInterfaceBoard) {
        this.amountInterfaceBoard = amountInterfaceBoard;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public Contract startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getFinishTime() {
        return this.finishTime;
    }

    public Contract finishTime(ZonedDateTime finishTime) {
        this.setFinishTime(finishTime);
        return this;
    }

    public void setFinishTime(ZonedDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Contract operator(Operator operator) {
        this.setOperator(operator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return id != null && id.equals(((Contract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", type='" + getType() + "'" +
            ", amountInterfaceBoard=" + getAmountInterfaceBoard() +
            ", startTime='" + getStartTime() + "'" +
            ", finishTime='" + getFinishTime() + "'" +
            "}";
    }
}
