import { IInterfaceBoard, NewInterfaceBoard } from './interface-board.model';

export const sampleWithRequiredData: IInterfaceBoard = {
  id: 42516,
  mac: 'payment Dinánmico Caserio',
};

export const sampleWithPartialData: IInterfaceBoard = {
  id: 12662,
  ipAddress: 'Senior',
  mac: 'Funcionalidad Honduras',
};

export const sampleWithFullData: IInterfaceBoard = {
  id: 97646,
  ipAddress: 'Cantabria withdrawal',
  hash: 'generating Amigable e-business',
  mac: 'Queso',
};

export const sampleWithNewData: NewInterfaceBoard = {
  mac: 'collaborative',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
