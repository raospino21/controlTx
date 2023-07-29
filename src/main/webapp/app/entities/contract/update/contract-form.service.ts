import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContract, NewContract } from '../contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContract for edit and NewContractFormGroupInput for create.
 */
type ContractFormGroupInput = IContract | PartialWithRequiredKeyOf<NewContract>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContract | NewContract> = Omit<T, 'startTime' | 'finishTime'> & {
  startTime?: string | null;
  finishTime?: string | null;
};

type ContractFormRawValue = FormValueOf<IContract>;

type NewContractFormRawValue = FormValueOf<NewContract>;

type ContractFormDefaults = Pick<NewContract, 'id' | 'startTime' | 'finishTime'>;

type ContractFormGroupContent = {
  id: FormControl<ContractFormRawValue['id'] | NewContract['id']>;
  reference: FormControl<ContractFormRawValue['reference']>;
  type: FormControl<ContractFormRawValue['type']>;
  numberInterfaceBoard: FormControl<ContractFormRawValue['numberInterfaceBoard']>;
  startTime: FormControl<ContractFormRawValue['startTime']>;
  finishTime: FormControl<ContractFormRawValue['finishTime']>;
  operator: FormControl<ContractFormRawValue['operator']>;
};

export type ContractFormGroup = FormGroup<ContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractFormService {
  createContractFormGroup(contract: ContractFormGroupInput = { id: null }): ContractFormGroup {
    const contractRawValue = this.convertContractToContractRawValue({
      ...this.getFormDefaults(),
      ...contract,
    });
    return new FormGroup<ContractFormGroupContent>({
      id: new FormControl(
        { value: contractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      reference: new FormControl(contractRawValue.reference),
      type: new FormControl(contractRawValue.type, {
        validators: [Validators.required],
      }),
      numberInterfaceBoard: new FormControl(contractRawValue.numberInterfaceBoard, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(contractRawValue.startTime, {
        validators: [Validators.required],
      }),
      finishTime: new FormControl(contractRawValue.finishTime),
      operator: new FormControl(contractRawValue.operator, {
        validators: [Validators.required],
      }),
    });
  }

  getContract(form: ContractFormGroup): IContract | NewContract {
    return this.convertContractRawValueToContract(form.getRawValue() as ContractFormRawValue | NewContractFormRawValue);
  }

  resetForm(form: ContractFormGroup, contract: ContractFormGroupInput): void {
    const contractRawValue = this.convertContractToContractRawValue({ ...this.getFormDefaults(), ...contract });
    form.reset(
      {
        ...contractRawValue,
        id: { value: contractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContractFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      finishTime: currentTime,
    };
  }

  private convertContractRawValueToContract(rawContract: ContractFormRawValue | NewContractFormRawValue): IContract | NewContract {
    return {
      ...rawContract,
      startTime: dayjs(rawContract.startTime, DATE_TIME_FORMAT),
      finishTime: dayjs(rawContract.finishTime, DATE_TIME_FORMAT),
    };
  }

  private convertContractToContractRawValue(
    contract: IContract | (Partial<NewContract> & ContractFormDefaults)
  ): ContractFormRawValue | PartialWithRequiredKeyOf<NewContractFormRawValue> {
    return {
      ...contract,
      startTime: contract.startTime ? contract.startTime.format(DATE_TIME_FORMAT) : undefined,
      finishTime: contract.finishTime ? contract.finishTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
