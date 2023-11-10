package co.com.ies.smol.service.dto.core;

import co.com.ies.smol.domain.enumeration.ContractType;
import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfoBoardByFileRecord implements Serializable {

    private static final long serialVersionUID = 5787683917777156451L;

    private String operatorName;
    private String contract;
    private ContractType type;
    private String mac;

    public InfoBoardByFileRecord() {
        super();
    }

    public InfoBoardByFileRecord(String operatorName, String contract, ContractType type, String mac) {
        this.operatorName = operatorName;
        this.contract = contract;
        this.type = type;
        this.mac = mac;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public ContractType getType() {
        return type;
    }

    public void setType(ContractType type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
