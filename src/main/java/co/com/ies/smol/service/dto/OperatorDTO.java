package co.com.ies.smol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.ies.smol.domain.Operator} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperatorDTO implements Serializable {

    private Long id;

    private String name;

    private String nit;

    private BrandDTO brand;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperatorDTO)) {
            return false;
        }

        OperatorDTO operatorDTO = (OperatorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operatorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperatorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nit='" + getNit() + "'" +
            ", brand=" + getBrand() +
            "}";
    }
}
