import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IControlInterfaceBoard, NewControlInterfaceBoard } from '../control-interface-board.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IControlInterfaceBoard for edit and NewControlInterfaceBoardFormGroupInput for create.
 */
type ControlInterfaceBoardFormGroupInput = IControlInterfaceBoard | PartialWithRequiredKeyOf<NewControlInterfaceBoard>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IControlInterfaceBoard | NewControlInterfaceBoard> = Omit<T, 'startTime' | 'finishTime'> & {
  startTime?: string | null;
  finishTime?: string | null;
};

type ControlInterfaceBoardFormRawValue = FormValueOf<IControlInterfaceBoard>;

type NewControlInterfaceBoardFormRawValue = FormValueOf<NewControlInterfaceBoard>;

type ControlInterfaceBoardFormDefaults = Pick<NewControlInterfaceBoard, 'id' | 'startTime' | 'finishTime'>;

type ControlInterfaceBoardFormGroupContent = {
  id: FormControl<ControlInterfaceBoardFormRawValue['id'] | NewControlInterfaceBoard['id']>;
  location: FormControl<ControlInterfaceBoardFormRawValue['location']>;
  state: FormControl<ControlInterfaceBoardFormRawValue['state']>;
  startTime: FormControl<ControlInterfaceBoardFormRawValue['startTime']>;
  finishTime: FormControl<ControlInterfaceBoardFormRawValue['finishTime']>;
  contract: FormControl<ControlInterfaceBoardFormRawValue['contract']>;
  interfaceBoard: FormControl<ControlInterfaceBoardFormRawValue['interfaceBoard']>;
};

export type ControlInterfaceBoardFormGroup = FormGroup<ControlInterfaceBoardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ControlInterfaceBoardFormService {
  createControlInterfaceBoardFormGroup(
    controlInterfaceBoard: ControlInterfaceBoardFormGroupInput = { id: null }
  ): ControlInterfaceBoardFormGroup {
    const controlInterfaceBoardRawValue = this.convertControlInterfaceBoardToControlInterfaceBoardRawValue({
      ...this.getFormDefaults(),
      ...controlInterfaceBoard,
    });
    return new FormGroup<ControlInterfaceBoardFormGroupContent>({
      id: new FormControl(
        { value: controlInterfaceBoardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      location: new FormControl(controlInterfaceBoardRawValue.location, {
        validators: [Validators.required],
      }),
      state: new FormControl(controlInterfaceBoardRawValue.state, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(controlInterfaceBoardRawValue.startTime, {
        validators: [Validators.required],
      }),
      finishTime: new FormControl(controlInterfaceBoardRawValue.finishTime),
      contract: new FormControl(controlInterfaceBoardRawValue.contract),
      interfaceBoard: new FormControl(controlInterfaceBoardRawValue.interfaceBoard, {
        validators: [Validators.required],
      }),
    });
  }

  getControlInterfaceBoard(form: ControlInterfaceBoardFormGroup): IControlInterfaceBoard | NewControlInterfaceBoard {
    return this.convertControlInterfaceBoardRawValueToControlInterfaceBoard(
      form.getRawValue() as ControlInterfaceBoardFormRawValue | NewControlInterfaceBoardFormRawValue
    );
  }

  resetForm(form: ControlInterfaceBoardFormGroup, controlInterfaceBoard: ControlInterfaceBoardFormGroupInput): void {
    const controlInterfaceBoardRawValue = this.convertControlInterfaceBoardToControlInterfaceBoardRawValue({
      ...this.getFormDefaults(),
      ...controlInterfaceBoard,
    });
    form.reset(
      {
        ...controlInterfaceBoardRawValue,
        id: { value: controlInterfaceBoardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ControlInterfaceBoardFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      finishTime: currentTime,
    };
  }

  private convertControlInterfaceBoardRawValueToControlInterfaceBoard(
    rawControlInterfaceBoard: ControlInterfaceBoardFormRawValue | NewControlInterfaceBoardFormRawValue
  ): IControlInterfaceBoard | NewControlInterfaceBoard {
    return {
      ...rawControlInterfaceBoard,
      startTime: dayjs(rawControlInterfaceBoard.startTime, DATE_TIME_FORMAT),
      finishTime: dayjs(rawControlInterfaceBoard.finishTime, DATE_TIME_FORMAT),
    };
  }

  private convertControlInterfaceBoardToControlInterfaceBoardRawValue(
    controlInterfaceBoard: IControlInterfaceBoard | (Partial<NewControlInterfaceBoard> & ControlInterfaceBoardFormDefaults)
  ): ControlInterfaceBoardFormRawValue | PartialWithRequiredKeyOf<NewControlInterfaceBoardFormRawValue> {
    return {
      ...controlInterfaceBoard,
      startTime: controlInterfaceBoard.startTime ? controlInterfaceBoard.startTime.format(DATE_TIME_FORMAT) : undefined,
      finishTime: controlInterfaceBoard.finishTime ? controlInterfaceBoard.finishTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
