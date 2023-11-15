import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IBoardDetailsInSotck {
  newQuantity?: number;
  usedQuantity?: number;
}

export class BoardDetailsInSotck implements IBoardDetailsInSotck {
  constructor(public newQuantity?: number, public usedQuantity?: number) {}
}
