package co.com.ies.smol.service.dto.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
public record FilterControlInterfaceBoard(String mac, String reference, String operatorName) implements Serializable {}
