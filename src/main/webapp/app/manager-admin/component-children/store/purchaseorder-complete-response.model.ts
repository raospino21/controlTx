import dayjs from 'dayjs/esm';

interface IReceptionOrder {
  id: number;
  providerLotNumber?: number;
  amountReceived?: number;
  remission?: string;
  entryDate?: dayjs.Dayjs;
  warrantyDate?: dayjs.Dayjs;
}

interface IPurchaseOrder {
  id: number;
  orderAmount?: number;
  createAt?: dayjs.Dayjs;
  iesOrderNumber?: number;
  amountReceived?: number;
}

export interface IPurchaseOrderCompleteResponse {
  purchaseOrder?: IPurchaseOrder;
  receptionOrderList?: IReceptionOrder[];
}

export class PurchaseOrderCompleteResponse implements IPurchaseOrderCompleteResponse {
  constructor(public purchaseOrder?: IPurchaseOrder, public receptionOrderList?: IReceptionOrder[]) {}
}
