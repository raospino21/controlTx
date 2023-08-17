import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';

export interface IInterfaceBoard {
  id: number;
  ipAddress?: string | null;
  hash?: string | null;
  mac?: string | null;
  receptionOrder?: Pick<IReceptionOrder, 'id' | 'providerLotNumber'> | null;
}

export type NewInterfaceBoard = Omit<IInterfaceBoard, 'id'> & { id: null };
