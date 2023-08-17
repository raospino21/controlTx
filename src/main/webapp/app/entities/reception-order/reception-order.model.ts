import dayjs from 'dayjs/esm';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';

export interface IReceptionOrder {
  id: number;
  providerLotNumber?: number | null;
  amountReceived?: number | null;
  remission?: string | null;
  entryDate?: dayjs.Dayjs | null;
  warrantyDate?: dayjs.Dayjs | null;
  purchaseOrder?: Pick<IPurchaseOrder, 'id' | 'iesOrderNumber'> | null;
}

export type NewReceptionOrder = Omit<IReceptionOrder, 'id'> & { id: null };
