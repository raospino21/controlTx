import { IOperator, NewOperator } from './operator.model';

export const sampleWithRequiredData: IOperator = {
  id: 23970,
};

export const sampleWithPartialData: IOperator = {
  id: 49523,
  name: 'Rampa Cuentas Conjunto',
  nit: 'Intranet',
};

export const sampleWithFullData: IOperator = {
  id: 20666,
  name: 'expedite Patatas',
  nit: 'virtual Cambridgeshire',
};

export const sampleWithNewData: NewOperator = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
