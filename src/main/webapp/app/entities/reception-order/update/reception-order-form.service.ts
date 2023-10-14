import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReceptionOrder, NewReceptionOrder } from '../reception-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReceptionOrder for edit and NewReceptionOrderFormGroupInput for create.
 */
type ReceptionOrderFormGroupInput = IReceptionOrder | PartialWithRequiredKeyOf<NewReceptionOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReceptionOrder | NewReceptionOrder> = Omit<T, 'entryDate' | 'warrantyDate'> & {
  entryDate?: string | null;
  warrantyDate?: string | null;
};

type ReceptionOrderFormRawValue = FormValueOf<IReceptionOrder>;

type NewReceptionOrderFormRawValue = FormValueOf<NewReceptionOrder>;

type ReceptionOrderFormDefaults = Pick<NewReceptionOrder, 'id' | 'entryDate' | 'warrantyDate'>;

type ReceptionOrderFormGroupContent = {
  id: FormControl<ReceptionOrderFormRawValue['id'] | NewReceptionOrder['id']>;
  providerLotNumber: FormControl<ReceptionOrderFormRawValue['providerLotNumber']>;
  amountReceived: FormControl<ReceptionOrderFormRawValue['amountReceived']>;
  remission: FormControl<ReceptionOrderFormRawValue['remission']>;
  entryDate: FormControl<ReceptionOrderFormRawValue['entryDate']>;
  warrantyDate: FormControl<ReceptionOrderFormRawValue['warrantyDate']>;
  purchaseOrder: FormControl<ReceptionOrderFormRawValue['purchaseOrder']>;
};

export type ReceptionOrderFormGroup = FormGroup<ReceptionOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReceptionOrderFormService {
  createReceptionOrderFormGroup(receptionOrder: ReceptionOrderFormGroupInput = { id: null }): ReceptionOrderFormGroup {
    const receptionOrderRawValue = this.convertReceptionOrderToReceptionOrderRawValue({
      ...this.getFormDefaults(),
      ...receptionOrder,
    });
    return new FormGroup<ReceptionOrderFormGroupContent>({
      id: new FormControl(
        { value: receptionOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      providerLotNumber: new FormControl(receptionOrderRawValue.providerLotNumber, {
        validators: [Validators.required],
      }),
      amountReceived: new FormControl(receptionOrderRawValue.amountReceived, {
        validators: [Validators.required],
      }),
      remission: new FormControl(receptionOrderRawValue.remission, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
      entryDate: new FormControl(receptionOrderRawValue.entryDate, {
        validators: [Validators.required],
      }),
      warrantyDate: new FormControl(receptionOrderRawValue.warrantyDate, {
        validators: [Validators.required],
      }),
      purchaseOrder: new FormControl(receptionOrderRawValue.purchaseOrder, {
        validators: [Validators.required],
      }),
    });
  }

  getReceptionOrder(form: ReceptionOrderFormGroup): IReceptionOrder | NewReceptionOrder {
    return this.convertReceptionOrderRawValueToReceptionOrder(
      form.getRawValue() as ReceptionOrderFormRawValue | NewReceptionOrderFormRawValue
    );
  }

  resetForm(form: ReceptionOrderFormGroup, receptionOrder: ReceptionOrderFormGroupInput): void {
    const receptionOrderRawValue = this.convertReceptionOrderToReceptionOrderRawValue({ ...this.getFormDefaults(), ...receptionOrder });
    form.reset(
      {
        ...receptionOrderRawValue,
        id: { value: receptionOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ReceptionOrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      entryDate: currentTime,
      warrantyDate: currentTime.add(1, 'year'),
    };
  }

  private convertReceptionOrderRawValueToReceptionOrder(
    rawReceptionOrder: ReceptionOrderFormRawValue | NewReceptionOrderFormRawValue
  ): IReceptionOrder | NewReceptionOrder {
    return {
      ...rawReceptionOrder,
      entryDate: dayjs(rawReceptionOrder.entryDate, DATE_TIME_FORMAT),
      warrantyDate: dayjs(rawReceptionOrder.warrantyDate, DATE_TIME_FORMAT),
    };
  }

  private convertReceptionOrderToReceptionOrderRawValue(
    receptionOrder: IReceptionOrder | (Partial<NewReceptionOrder> & ReceptionOrderFormDefaults)
  ): ReceptionOrderFormRawValue | PartialWithRequiredKeyOf<NewReceptionOrderFormRawValue> {
    return {
      ...receptionOrder,
      entryDate: receptionOrder.entryDate ? receptionOrder.entryDate.format(DATE_TIME_FORMAT) : undefined,
      warrantyDate: receptionOrder.warrantyDate ? receptionOrder.warrantyDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
