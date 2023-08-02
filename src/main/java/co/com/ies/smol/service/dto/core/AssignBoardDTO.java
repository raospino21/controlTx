package co.com.ies.smol.service.dto.core;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssignBoardDTO implements Serializable {

    @NotNull
    @NotBlank
    private String mac;

    @NotNull
    private String reference;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "AssignBoardDTO [mac=" + mac + ", reference=" + reference + "]";
    }
}
