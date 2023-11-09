import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IContractSub {
  amountInterfaceBoardContracted?: number;
  amountInterfaceBoardAssigned?: number;
  contractType?: ContractType;
  reference?: string;
  contractId?: number;
}

export class ContractSub implements IContractSub {
  constructor(
    public amountInterfaceBoardContracted?: number,
    public amountInterfaceBoardAssigned?: number,
    public contractType?: ContractType,
    public reference?: string,
    public contractId?: number
  ) {}
}
