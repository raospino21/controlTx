import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../interface-board.test-samples';

import { InterfaceBoardFormService } from './interface-board-form.service';

describe('InterfaceBoard Form Service', () => {
  let service: InterfaceBoardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InterfaceBoardFormService);
  });

  describe('Service methods', () => {
    describe('createInterfaceBoardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInterfaceBoardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ipAddress: expect.any(Object),
            hash: expect.any(Object),
            mac: expect.any(Object),
            receptionOrder: expect.any(Object),
          })
        );
      });

      it('passing IInterfaceBoard should create a new form with FormGroup', () => {
        const formGroup = service.createInterfaceBoardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ipAddress: expect.any(Object),
            hash: expect.any(Object),
            mac: expect.any(Object),
            receptionOrder: expect.any(Object),
          })
        );
      });
    });

    describe('getInterfaceBoard', () => {
      it('should return NewInterfaceBoard for default InterfaceBoard initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInterfaceBoardFormGroup(sampleWithNewData);

        const interfaceBoard = service.getInterfaceBoard(formGroup) as any;

        expect(interfaceBoard).toMatchObject(sampleWithNewData);
      });

      it('should return NewInterfaceBoard for empty InterfaceBoard initial value', () => {
        const formGroup = service.createInterfaceBoardFormGroup();

        const interfaceBoard = service.getInterfaceBoard(formGroup) as any;

        expect(interfaceBoard).toMatchObject({});
      });

      it('should return IInterfaceBoard', () => {
        const formGroup = service.createInterfaceBoardFormGroup(sampleWithRequiredData);

        const interfaceBoard = service.getInterfaceBoard(formGroup) as any;

        expect(interfaceBoard).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInterfaceBoard should not enable id FormControl', () => {
        const formGroup = service.createInterfaceBoardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInterfaceBoard should disable id FormControl', () => {
        const formGroup = service.createInterfaceBoardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
