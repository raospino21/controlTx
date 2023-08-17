import dayjs from 'dayjs/esm';

import { IReceptionOrder, NewReceptionOrder } from './reception-order.model';

export const sampleWithRequiredData: IReceptionOrder = {
  id: 17389,
  providerLotNumber: 25413,
  amountReceived: 4807,
  remission: 'Rústico',
  entryDate: dayjs('2023-08-17T04:24'),
  warrantyDate: dayjs('2023-08-17T05:01'),
};

export const sampleWithPartialData: IReceptionOrder = {
  id: 84952,
  providerLotNumber: 31950,
  amountReceived: 44805,
  remission: 'interface Diverso Sincronizado',
  entryDate: dayjs('2023-08-17T06:31'),
  warrantyDate: dayjs('2023-08-16T18:14'),
};

export const sampleWithFullData: IReceptionOrder = {
  id: 6826,
  providerLotNumber: 31461,
  amountReceived: 57943,
  remission: 'solutions',
  entryDate: dayjs('2023-08-16T23:47'),
  warrantyDate: dayjs('2023-08-17T07:16'),
};

export const sampleWithNewData: NewReceptionOrder = {
  providerLotNumber: 95111,
  amountReceived: 73471,
  remission: 'Savings',
  entryDate: dayjs('2023-08-17T14:13'),
  warrantyDate: dayjs('2023-08-17T15:30'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
