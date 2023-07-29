import { IDataSheetInterface } from 'app/entities/data-sheet-interface/data-sheet-interface.model';

export interface IInterfaceBoard {
  id: number;
  ipAddress?: string | null;
  hash?: string | null;
  mac?: string | null;
  dataSheetInterface?: Pick<IDataSheetInterface, 'id'> | null;
}

export type NewInterfaceBoard = Omit<IInterfaceBoard, 'id'> & { id: null };
