import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IRequestStatus {
  title?: string;
  msg?: string;
  code?: number | null;
}

export class RequestStatus implements IRequestStatus {
  constructor(public title?: string, public msg?: string, public code?: number) {}
}
