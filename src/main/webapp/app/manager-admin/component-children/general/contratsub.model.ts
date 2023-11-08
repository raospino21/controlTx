import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IContractSub {
  amountInterfaceBoardContracted?: number;
  amountInterfaceBoardAssigned?: number;
  contractType?: ContractType;
  reference?: string;
}

export class ContractSub implements IContractSub {
  constructor(
    public amountInterfaceBoardContracted?: number,
    public amountInterfaceBoardAssigned?: number,
    public contractType?: ContractType,
    public reference?: string
  ) {}
}
