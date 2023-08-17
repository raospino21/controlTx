import dayjs from 'dayjs/esm';

import { ContractType } from 'app/entities/enumerations/contract-type.model';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 90219,
  type: ContractType['LOAN'],
  amountInterfaceBoard: 7385,
  startTime: dayjs('2021-09-26T21:33'),
};

export const sampleWithPartialData: IContract = {
  id: 35474,
  type: ContractType['SALE'],
  amountInterfaceBoard: 19282,
  startTime: dayjs('2021-09-26T21:14'),
  finishTime: dayjs('2021-09-27T18:28'),
};

export const sampleWithFullData: IContract = {
  id: 52963,
  reference: 'conglomeración Deportes',
  type: ContractType['LOAN'],
  amountInterfaceBoard: 76933,
  startTime: dayjs('2021-09-26T23:54'),
  finishTime: dayjs('2021-09-27T05:09'),
};

export const sampleWithNewData: NewContract = {
  type: ContractType['LOAN'],
  amountInterfaceBoard: 8129,
  startTime: dayjs('2021-09-27T15:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
