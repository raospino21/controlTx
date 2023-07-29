import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDataSheetInterface, NewDataSheetInterface } from '../data-sheet-interface.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDataSheetInterface for edit and NewDataSheetInterfaceFormGroupInput for create.
 */
type DataSheetInterfaceFormGroupInput = IDataSheetInterface | PartialWithRequiredKeyOf<NewDataSheetInterface>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDataSheetInterface | NewDataSheetInterface> = Omit<T, 'entryDate'> & {
  entryDate?: string | null;
};

type DataSheetInterfaceFormRawValue = FormValueOf<IDataSheetInterface>;

type NewDataSheetInterfaceFormRawValue = FormValueOf<NewDataSheetInterface>;

type DataSheetInterfaceFormDefaults = Pick<NewDataSheetInterface, 'id' | 'entryDate'>;

type DataSheetInterfaceFormGroupContent = {
  id: FormControl<DataSheetInterfaceFormRawValue['id'] | NewDataSheetInterface['id']>;
  colcircuitosLotNumber: FormControl<DataSheetInterfaceFormRawValue['colcircuitosLotNumber']>;
  orderAmount: FormControl<DataSheetInterfaceFormRawValue['orderAmount']>;
  amountReceived: FormControl<DataSheetInterfaceFormRawValue['amountReceived']>;
  remission: FormControl<DataSheetInterfaceFormRawValue['remission']>;
  entryDate: FormControl<DataSheetInterfaceFormRawValue['entryDate']>;
  iesOrderNumber: FormControl<DataSheetInterfaceFormRawValue['iesOrderNumber']>;
};

export type DataSheetInterfaceFormGroup = FormGroup<DataSheetInterfaceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DataSheetInterfaceFormService {
  createDataSheetInterfaceFormGroup(dataSheetInterface: DataSheetInterfaceFormGroupInput = { id: null }): DataSheetInterfaceFormGroup {
    const dataSheetInterfaceRawValue = this.convertDataSheetInterfaceToDataSheetInterfaceRawValue({
      ...this.getFormDefaults(),
      ...dataSheetInterface,
    });
    return new FormGroup<DataSheetInterfaceFormGroupContent>({
      id: new FormControl(
        { value: dataSheetInterfaceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      colcircuitosLotNumber: new FormControl(dataSheetInterfaceRawValue.colcircuitosLotNumber, {
        validators: [Validators.required],
      }),
      orderAmount: new FormControl(dataSheetInterfaceRawValue.orderAmount, {
        validators: [Validators.required],
      }),
      amountReceived: new FormControl(dataSheetInterfaceRawValue.amountReceived, {
        validators: [Validators.required],
      }),
      remission: new FormControl(dataSheetInterfaceRawValue.remission, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
      entryDate: new FormControl(dataSheetInterfaceRawValue.entryDate, {
        validators: [Validators.required],
      }),
      iesOrderNumber: new FormControl(dataSheetInterfaceRawValue.iesOrderNumber, {
        validators: [Validators.required],
      }),
    });
  }

  getDataSheetInterface(form: DataSheetInterfaceFormGroup): IDataSheetInterface | NewDataSheetInterface {
    return this.convertDataSheetInterfaceRawValueToDataSheetInterface(
      form.getRawValue() as DataSheetInterfaceFormRawValue | NewDataSheetInterfaceFormRawValue
    );
  }

  resetForm(form: DataSheetInterfaceFormGroup, dataSheetInterface: DataSheetInterfaceFormGroupInput): void {
    const dataSheetInterfaceRawValue = this.convertDataSheetInterfaceToDataSheetInterfaceRawValue({
      ...this.getFormDefaults(),
      ...dataSheetInterface,
    });
    form.reset(
      {
        ...dataSheetInterfaceRawValue,
        id: { value: dataSheetInterfaceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DataSheetInterfaceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      entryDate: currentTime,
    };
  }

  private convertDataSheetInterfaceRawValueToDataSheetInterface(
    rawDataSheetInterface: DataSheetInterfaceFormRawValue | NewDataSheetInterfaceFormRawValue
  ): IDataSheetInterface | NewDataSheetInterface {
    return {
      ...rawDataSheetInterface,
      entryDate: dayjs(rawDataSheetInterface.entryDate, DATE_TIME_FORMAT),
    };
  }

  private convertDataSheetInterfaceToDataSheetInterfaceRawValue(
    dataSheetInterface: IDataSheetInterface | (Partial<NewDataSheetInterface> & DataSheetInterfaceFormDefaults)
  ): DataSheetInterfaceFormRawValue | PartialWithRequiredKeyOf<NewDataSheetInterfaceFormRawValue> {
    return {
      ...dataSheetInterface,
      entryDate: dataSheetInterface.entryDate ? dataSheetInterface.entryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
