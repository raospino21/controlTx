import dayjs from 'dayjs/esm';

export interface IDataSheetInterface {
  id: number;
  colcircuitosLotNumber?: number | null;
  orderAmount?: number | null;
  amountReceived?: number | null;
  remission?: string | null;
  entryDate?: dayjs.Dayjs | null;
  iesOrderNumber?: number | null;
}

export type NewDataSheetInterface = Omit<IDataSheetInterface, 'id'> & { id: null };
