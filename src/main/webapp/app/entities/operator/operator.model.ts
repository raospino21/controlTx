import { IBrand } from 'app/entities/brand/brand.model';

export interface IOperator {
  id: number;
  name?: string | null;
  nit?: string | null;
  brand?: Pick<IBrand, 'id' | 'name'> | null;
}

export type NewOperator = Omit<IOperator, 'id'> & { id: null };
