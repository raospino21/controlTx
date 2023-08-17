package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.InterfaceBoard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterfaceBoardDTO implements Serializable {

    private Long id;

    private String ipAddress;

    private String hash;

    @NotNull
    private String mac;

    private ReceptionOrderDTO receptionOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public ReceptionOrderDTO getReceptionOrder() {
        return receptionOrder;
    }

    public void setReceptionOrder(ReceptionOrderDTO receptionOrder) {
        this.receptionOrder = receptionOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterfaceBoardDTO)) {
            return false;
        }

        InterfaceBoardDTO interfaceBoardDTO = (InterfaceBoardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, interfaceBoardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterfaceBoardDTO{" +
            "id=" + getId() +
            ", ipAddress='" + getIpAddress() + "'" +
            ", hash='" + getHash() + "'" +
            ", mac='" + getMac() + "'" +
            ", receptionOrder=" + getReceptionOrder() +
            "}";
    }
}
