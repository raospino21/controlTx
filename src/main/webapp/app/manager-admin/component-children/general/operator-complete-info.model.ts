import { IOperator } from 'app/entities/operator/operator.model';
import { IContractSub } from './contratsub.model';

export interface IOperatorCompleteInfo {
  operator?: IOperator;
  contractSubList?: IContractSub[];
}

export class OperatorCompleteInfo implements IOperatorCompleteInfo {
  constructor(public operator?: IOperator, public contractSubList?: IContractSub[]) {}
}
