import { IBrand } from 'app/entities/brand/brand.model';

export interface IBrandCompleteInfo {
  brand?: IBrand;
  totalAmountBoardAssigned?: number;
  totalAmountBoardcontracted?: number;
}

export class BrandCompleteInfo implements IBrandCompleteInfo {
  constructor(public brand?: IBrand, public totalAmountBoardAssigned?: number, public totalAmountBoardcontracted?: number) {}
}
