export interface IOrderPurchaseGeneralDetail {
  amountPurchase?: number;
  amountReceived?: number;
  amountInterfaceBoardTotal?: number;
}

export class OrderPurchaseGeneralDetail implements IOrderPurchaseGeneralDetail {
  constructor(public amountPurchase?: number, public amountReceived?: number, public amountInterfaceBoardTotal?: number) {}
}
