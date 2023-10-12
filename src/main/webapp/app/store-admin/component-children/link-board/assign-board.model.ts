import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IAssignBoard {
  reference?: string | null;
  contractType?: ContractType | null;
  macs?: string[] | null;
}

export class AssignBoard implements IAssignBoard {
  constructor(public reference?: string, public contractType?: ContractType, public macs?: string[]) {}
}
