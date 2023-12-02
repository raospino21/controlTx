package co.com.ies.smol.service.dto.core;

import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public record OrderPurchaseGeneralDetail(int amountPurchase, int amountReceived, int amountInterfaceBoardTotal) implements Serializable {}
