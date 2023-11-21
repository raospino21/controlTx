export interface IOrderReceptionDetail {
  associatedAmount?: number;
  validatedAmount?: number;
}

export class OrderReceptionDetail implements IOrderReceptionDetail {
  constructor(public associatedAmount?: number, public validatedAmount?: number) {}
}
