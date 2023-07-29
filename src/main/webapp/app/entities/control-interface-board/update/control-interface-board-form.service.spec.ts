import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../control-interface-board.test-samples';

import { ControlInterfaceBoardFormService } from './control-interface-board-form.service';

describe('ControlInterfaceBoard Form Service', () => {
  let service: ControlInterfaceBoardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ControlInterfaceBoardFormService);
  });

  describe('Service methods', () => {
    describe('createControlInterfaceBoardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createControlInterfaceBoardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            location: expect.any(Object),
            state: expect.any(Object),
            startTime: expect.any(Object),
            finishTime: expect.any(Object),
            contract: expect.any(Object),
            interfaceBoard: expect.any(Object),
          })
        );
      });

      it('passing IControlInterfaceBoard should create a new form with FormGroup', () => {
        const formGroup = service.createControlInterfaceBoardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            location: expect.any(Object),
            state: expect.any(Object),
            startTime: expect.any(Object),
            finishTime: expect.any(Object),
            contract: expect.any(Object),
            interfaceBoard: expect.any(Object),
          })
        );
      });
    });

    describe('getControlInterfaceBoard', () => {
      it('should return NewControlInterfaceBoard for default ControlInterfaceBoard initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createControlInterfaceBoardFormGroup(sampleWithNewData);

        const controlInterfaceBoard = service.getControlInterfaceBoard(formGroup) as any;

        expect(controlInterfaceBoard).toMatchObject(sampleWithNewData);
      });

      it('should return NewControlInterfaceBoard for empty ControlInterfaceBoard initial value', () => {
        const formGroup = service.createControlInterfaceBoardFormGroup();

        const controlInterfaceBoard = service.getControlInterfaceBoard(formGroup) as any;

        expect(controlInterfaceBoard).toMatchObject({});
      });

      it('should return IControlInterfaceBoard', () => {
        const formGroup = service.createControlInterfaceBoardFormGroup(sampleWithRequiredData);

        const controlInterfaceBoard = service.getControlInterfaceBoard(formGroup) as any;

        expect(controlInterfaceBoard).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IControlInterfaceBoard should not enable id FormControl', () => {
        const formGroup = service.createControlInterfaceBoardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewControlInterfaceBoard should disable id FormControl', () => {
        const formGroup = service.createControlInterfaceBoardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
