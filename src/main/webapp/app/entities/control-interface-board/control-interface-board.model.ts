import dayjs from 'dayjs/esm';
import { IContract } from 'app/entities/contract/contract.model';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { Location } from 'app/entities/enumerations/location.model';
import { StatusInterfaceBoard } from 'app/entities/enumerations/status-interface-board.model';

export interface IControlInterfaceBoard {
  id: number;
  location?: Location | null;
  state?: StatusInterfaceBoard | null;
  startTime?: dayjs.Dayjs | null;
  finishTime?: dayjs.Dayjs | null;
  contract?: Pick<IContract, 'id' | 'reference'> | null;
  interfaceBoard?: Pick<IInterfaceBoard, 'id' | 'mac'> | null;
}

export type NewControlInterfaceBoard = Omit<IControlInterfaceBoard, 'id'> & { id: null };
