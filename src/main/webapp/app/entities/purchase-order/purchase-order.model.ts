import dayjs from 'dayjs/esm';

export interface IPurchaseOrder {
  id: number;
  orderAmount?: number | null;
  createAt?: dayjs.Dayjs | null;
  iesOrderNumber?: number | null;
}

export type NewPurchaseOrder = Omit<IPurchaseOrder, 'id'> & { id: null };
