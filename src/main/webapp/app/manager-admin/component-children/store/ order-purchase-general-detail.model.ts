export interface IOrderPurchaseGeneralDetail {
  amountPurchase?: number;
  amountReceived?: number;
}

export class OrderPurchaseGeneralDetail implements IOrderPurchaseGeneralDetail {
  constructor(public amountPurchase?: number, public amountReceived?: number) {}
}
