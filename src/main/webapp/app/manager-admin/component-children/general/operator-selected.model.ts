import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { IOperator } from 'app/entities/operator/operator.model';

export interface IOperatorSelected {
  operator?: IOperator;
  contractType?: ContractType;
  reference?: string;
}

export class OperatorSelected implements IOperatorSelected {
  constructor(public operator?: IOperator, public contractType?: ContractType, public reference?: string) {}
}
