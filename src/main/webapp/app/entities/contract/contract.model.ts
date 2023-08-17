import dayjs from 'dayjs/esm';
import { IOperator } from 'app/entities/operator/operator.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IContract {
  id: number;
  reference?: string | null;
  type?: ContractType | null;
  amountInterfaceBoard?: number | null;
  startTime?: dayjs.Dayjs | null;
  finishTime?: dayjs.Dayjs | null;
  operator?: Pick<IOperator, 'id' | 'name'> | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
