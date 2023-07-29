import dayjs from 'dayjs/esm';

import { Location } from 'app/entities/enumerations/location.model';
import { StatusInterfaceBoard } from 'app/entities/enumerations/status-interface-board.model';

import { IControlInterfaceBoard, NewControlInterfaceBoard } from './control-interface-board.model';

export const sampleWithRequiredData: IControlInterfaceBoard = {
  id: 28039,
  location: Location['CLIENT'],
  state: StatusInterfaceBoard['STOCK'],
  startTime: dayjs('2021-09-26T20:15'),
};

export const sampleWithPartialData: IControlInterfaceBoard = {
  id: 8848,
  location: Location['IES'],
  state: StatusInterfaceBoard['OPERATION'],
  startTime: dayjs('2021-09-27T15:56'),
};

export const sampleWithFullData: IControlInterfaceBoard = {
  id: 27652,
  location: Location['IES'],
  state: StatusInterfaceBoard['STOCK'],
  startTime: dayjs('2021-09-27T18:05'),
  finishTime: dayjs('2021-09-27T11:55'),
};

export const sampleWithNewData: NewControlInterfaceBoard = {
  location: Location['IES'],
  state: StatusInterfaceBoard['IN_TRANSFER'],
  startTime: dayjs('2021-09-27T18:32'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
