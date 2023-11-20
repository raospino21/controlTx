import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPurchaseOrder, NewPurchaseOrder } from '../purchase-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPurchaseOrder for edit and NewPurchaseOrderFormGroupInput for create.
 */
type PurchaseOrderFormGroupInput = IPurchaseOrder | PartialWithRequiredKeyOf<NewPurchaseOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPurchaseOrder | NewPurchaseOrder> = Omit<T, 'createAt'> & {
  createAt?: string | null;
};

type PurchaseOrderFormRawValue = FormValueOf<IPurchaseOrder>;

type NewPurchaseOrderFormRawValue = FormValueOf<NewPurchaseOrder>;

type PurchaseOrderFormDefaults = Pick<NewPurchaseOrder, 'id' | 'createAt' | 'iesOrderNumber'>;

type PurchaseOrderFormGroupContent = {
  id: FormControl<PurchaseOrderFormRawValue['id'] | NewPurchaseOrder['id']>;
  orderAmount: FormControl<PurchaseOrderFormRawValue['orderAmount']>;
  createAt: FormControl<PurchaseOrderFormRawValue['createAt']>;
  iesOrderNumber: FormControl<PurchaseOrderFormRawValue['iesOrderNumber']>;
};

export type PurchaseOrderFormGroup = FormGroup<PurchaseOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderFormService {
  createPurchaseOrderFormGroup(purchaseOrder: PurchaseOrderFormGroupInput = { id: null }): PurchaseOrderFormGroup {
    const purchaseOrderRawValue = this.convertPurchaseOrderToPurchaseOrderRawValue({
      ...this.getFormDefaults(),
      ...purchaseOrder,
    });
    return new FormGroup<PurchaseOrderFormGroupContent>({
      id: new FormControl(
        { value: purchaseOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      orderAmount: new FormControl(purchaseOrderRawValue.orderAmount, {
        validators: [Validators.required],
      }),
      createAt: new FormControl(purchaseOrderRawValue.createAt, {
        validators: [Validators.required],
      }),
      iesOrderNumber: new FormControl(purchaseOrderRawValue.iesOrderNumber, {
        validators: [Validators.required],
      }),
    });
  }

  getPurchaseOrder(form: PurchaseOrderFormGroup): IPurchaseOrder | NewPurchaseOrder {
    return this.convertPurchaseOrderRawValueToPurchaseOrder(form.getRawValue() as PurchaseOrderFormRawValue | NewPurchaseOrderFormRawValue);
  }

  resetForm(form: PurchaseOrderFormGroup, purchaseOrder: PurchaseOrderFormGroupInput): void {
    const purchaseOrderRawValue = this.convertPurchaseOrderToPurchaseOrderRawValue({ ...this.getFormDefaults(), ...purchaseOrder });
    form.reset(
      {
        ...purchaseOrderRawValue,
        id: { value: purchaseOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PurchaseOrderFormDefaults {
    const currentTime = dayjs();
    const iesOrderNumberGenerate = Math.floor(10000000 + Math.random() * 90000000);

    return {
      id: null,
      createAt: currentTime,
      iesOrderNumber: iesOrderNumberGenerate,
    };
  }

  private convertPurchaseOrderRawValueToPurchaseOrder(
    rawPurchaseOrder: PurchaseOrderFormRawValue | NewPurchaseOrderFormRawValue
  ): IPurchaseOrder | NewPurchaseOrder {
    return {
      ...rawPurchaseOrder,
      createAt: dayjs(rawPurchaseOrder.createAt, DATE_TIME_FORMAT),
    };
  }

  private convertPurchaseOrderToPurchaseOrderRawValue(
    purchaseOrder: IPurchaseOrder | (Partial<NewPurchaseOrder> & PurchaseOrderFormDefaults)
  ): PurchaseOrderFormRawValue | PartialWithRequiredKeyOf<NewPurchaseOrderFormRawValue> {
    return {
      ...purchaseOrder,
      createAt: purchaseOrder.createAt ? purchaseOrder.createAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
