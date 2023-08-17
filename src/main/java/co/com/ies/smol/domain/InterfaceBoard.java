package co.com.ies.smol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InterfaceBoard.
 */
@Entity
@Table(name = "interface_board")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterfaceBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "hash")
    private String hash;

    @NotNull
    @Column(name = "mac", nullable = false)
    private String mac;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "purchaseOrder" }, allowSetters = true)
    private ReceptionOrder receptionOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InterfaceBoard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public InterfaceBoard ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHash() {
        return this.hash;
    }

    public InterfaceBoard hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMac() {
        return this.mac;
    }

    public InterfaceBoard mac(String mac) {
        this.setMac(mac);
        return this;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public ReceptionOrder getReceptionOrder() {
        return this.receptionOrder;
    }

    public void setReceptionOrder(ReceptionOrder receptionOrder) {
        this.receptionOrder = receptionOrder;
    }

    public InterfaceBoard receptionOrder(ReceptionOrder receptionOrder) {
        this.setReceptionOrder(receptionOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterfaceBoard)) {
            return false;
        }
        return id != null && id.equals(((InterfaceBoard) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterfaceBoard{" +
            "id=" + getId() +
            ", ipAddress='" + getIpAddress() + "'" +
            ", hash='" + getHash() + "'" +
            ", mac='" + getMac() + "'" +
            "}";
    }
}
