import dayjs from 'dayjs/esm';

import { IPurchaseOrder, NewPurchaseOrder } from './purchase-order.model';

export const sampleWithRequiredData: IPurchaseOrder = {
  id: 1775,
  orderAmount: 64701,
  createAt: dayjs('2023-08-17T11:36'),
  iesOrderNumber: 63194,
};

export const sampleWithPartialData: IPurchaseOrder = {
  id: 81332,
  orderAmount: 20490,
  createAt: dayjs('2023-08-17T06:09'),
  iesOrderNumber: 24440,
};

export const sampleWithFullData: IPurchaseOrder = {
  id: 60324,
  orderAmount: 28038,
  createAt: dayjs('2023-08-17T05:50'),
  iesOrderNumber: 12768,
};

export const sampleWithNewData: NewPurchaseOrder = {
  orderAmount: 21806,
  createAt: dayjs('2023-08-17T03:51'),
  iesOrderNumber: 25171,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
