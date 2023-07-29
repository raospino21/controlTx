import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOperator, NewOperator } from '../operator.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperator for edit and NewOperatorFormGroupInput for create.
 */
type OperatorFormGroupInput = IOperator | PartialWithRequiredKeyOf<NewOperator>;

type OperatorFormDefaults = Pick<NewOperator, 'id'>;

type OperatorFormGroupContent = {
  id: FormControl<IOperator['id'] | NewOperator['id']>;
  name: FormControl<IOperator['name']>;
  nit: FormControl<IOperator['nit']>;
  brand: FormControl<IOperator['brand']>;
};

export type OperatorFormGroup = FormGroup<OperatorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperatorFormService {
  createOperatorFormGroup(operator: OperatorFormGroupInput = { id: null }): OperatorFormGroup {
    const operatorRawValue = {
      ...this.getFormDefaults(),
      ...operator,
    };
    return new FormGroup<OperatorFormGroupContent>({
      id: new FormControl(
        { value: operatorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(operatorRawValue.name),
      nit: new FormControl(operatorRawValue.nit),
      brand: new FormControl(operatorRawValue.brand, {
        validators: [Validators.required],
      }),
    });
  }

  getOperator(form: OperatorFormGroup): IOperator | NewOperator {
    return form.getRawValue() as IOperator | NewOperator;
  }

  resetForm(form: OperatorFormGroup, operator: OperatorFormGroupInput): void {
    const operatorRawValue = { ...this.getFormDefaults(), ...operator };
    form.reset(
      {
        ...operatorRawValue,
        id: { value: operatorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OperatorFormDefaults {
    return {
      id: null,
    };
  }
}
