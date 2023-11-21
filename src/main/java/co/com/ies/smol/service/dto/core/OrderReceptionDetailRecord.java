package co.com.ies.smol.service.dto.core;

import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record OrderReceptionDetailRecord(int associatedAmount, int validatedAmount) implements Serializable {}
