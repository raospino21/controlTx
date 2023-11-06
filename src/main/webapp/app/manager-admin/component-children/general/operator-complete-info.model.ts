import { IOperator } from 'app/entities/operator/operator.model';

export interface IOperatorCompleteInfo {
  operator?: IOperator;
  totalAmountBoardAssigned?: number;
  totalAmountBoardcontracted?: number;
}

export class OperatorCompleteInfo implements IOperatorCompleteInfo {
  constructor(public operator?: IOperator, public totalAmountBoardAssigned?: number, public totalAmountBoardcontracted?: number) {}
}
