package co.com.ies.smol.service.dto.core;

import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record InfoBoardToAssignByFileRecord(String descripcion, String mac, String nombreOperador, String contracto, String tipo)
    implements Serializable {
    private static final long serialVersionUID = 5787683917777156451L;
}
