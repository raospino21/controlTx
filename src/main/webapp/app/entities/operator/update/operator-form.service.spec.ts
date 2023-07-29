import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../operator.test-samples';

import { OperatorFormService } from './operator-form.service';

describe('Operator Form Service', () => {
  let service: OperatorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OperatorFormService);
  });

  describe('Service methods', () => {
    describe('createOperatorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOperatorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            nit: expect.any(Object),
            brand: expect.any(Object),
          })
        );
      });

      it('passing IOperator should create a new form with FormGroup', () => {
        const formGroup = service.createOperatorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            nit: expect.any(Object),
            brand: expect.any(Object),
          })
        );
      });
    });

    describe('getOperator', () => {
      it('should return NewOperator for default Operator initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOperatorFormGroup(sampleWithNewData);

        const operator = service.getOperator(formGroup) as any;

        expect(operator).toMatchObject(sampleWithNewData);
      });

      it('should return NewOperator for empty Operator initial value', () => {
        const formGroup = service.createOperatorFormGroup();

        const operator = service.getOperator(formGroup) as any;

        expect(operator).toMatchObject({});
      });

      it('should return IOperator', () => {
        const formGroup = service.createOperatorFormGroup(sampleWithRequiredData);

        const operator = service.getOperator(formGroup) as any;

        expect(operator).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOperator should not enable id FormControl', () => {
        const formGroup = service.createOperatorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOperator should disable id FormControl', () => {
        const formGroup = service.createOperatorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
