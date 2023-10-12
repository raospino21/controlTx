import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IRequestStatus {
  msg?: string;
  code?: number | null;
}

export class RequestStatus implements IRequestStatus {
  constructor(public msg?: string, public code?: number) {}
}
