import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IAssignBoard {
  reference?: string;
  contractType?: ContractType;
  amountAssociate?: number;
}

export class AssignBoard implements IAssignBoard {
  constructor(public reference?: string, public contractType?: ContractType, public amountAssociate?: number) {}
}
