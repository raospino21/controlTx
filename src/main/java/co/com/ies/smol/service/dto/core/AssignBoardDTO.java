package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.domain.enumeration.ContractType;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssignBoardDTO implements Serializable {

    @NotNull
    private List<String> macs;

    @NotNull
    @NotBlank
    private String reference;

    @NotNull
    ContractType contractType;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getMacs() {
        return macs;
    }

    public void setMacs(List<String> macs) {
        this.macs = macs;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    @Override
    public String toString() {
        return "AssignBoardDTO [macs=" + macs + ", reference=" + reference + ", contractType=" + contractType + "]";
    }
}
