import dayjs from 'dayjs/esm';

import { IDataSheetInterface, NewDataSheetInterface } from './data-sheet-interface.model';

export const sampleWithRequiredData: IDataSheetInterface = {
  id: 17989,
  colcircuitosLotNumber: 15205,
  orderAmount: 11833,
  amountReceived: 15077,
  remission: 'Programa program transmitter',
  entryDate: dayjs('2021-09-27T14:09'),
  iesOrderNumber: 34275,
};

export const sampleWithPartialData: IDataSheetInterface = {
  id: 10103,
  colcircuitosLotNumber: 52448,
  orderAmount: 67132,
  amountReceived: 12776,
  remission: 'Eslovaquia',
  entryDate: dayjs('2021-09-27T12:38'),
  iesOrderNumber: 35581,
};

export const sampleWithFullData: IDataSheetInterface = {
  id: 45211,
  colcircuitosLotNumber: 89983,
  orderAmount: 6380,
  amountReceived: 10977,
  remission: 'Bedfordshire',
  entryDate: dayjs('2021-09-27T09:43'),
  iesOrderNumber: 17818,
};

export const sampleWithNewData: NewDataSheetInterface = {
  colcircuitosLotNumber: 77788,
  orderAmount: 12289,
  amountReceived: 51249,
  remission: 'Rioja synthesize',
  entryDate: dayjs('2021-09-26T20:07'),
  iesOrderNumber: 63520,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
