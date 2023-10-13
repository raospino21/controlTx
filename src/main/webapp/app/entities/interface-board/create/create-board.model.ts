import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';

export interface ICreateBoard {
  receptionOrder?: IReceptionOrder | null;
  macs?: string[] | null;
}

export class CreateBoard implements ICreateBoard {
  constructor(public receptionOrder?: IReceptionOrder, public macs?: string[]) {}
}
