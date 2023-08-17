import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../reception-order.test-samples';

import { ReceptionOrderFormService } from './reception-order-form.service';

describe('ReceptionOrder Form Service', () => {
  let service: ReceptionOrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReceptionOrderFormService);
  });

  describe('Service methods', () => {
    describe('createReceptionOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReceptionOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            providerLotNumber: expect.any(Object),
            amountReceived: expect.any(Object),
            remission: expect.any(Object),
            entryDate: expect.any(Object),
            warrantyDate: expect.any(Object),
            purchaseOrder: expect.any(Object),
          })
        );
      });

      it('passing IReceptionOrder should create a new form with FormGroup', () => {
        const formGroup = service.createReceptionOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            providerLotNumber: expect.any(Object),
            amountReceived: expect.any(Object),
            remission: expect.any(Object),
            entryDate: expect.any(Object),
            warrantyDate: expect.any(Object),
            purchaseOrder: expect.any(Object),
          })
        );
      });
    });

    describe('getReceptionOrder', () => {
      it('should return NewReceptionOrder for default ReceptionOrder initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createReceptionOrderFormGroup(sampleWithNewData);

        const receptionOrder = service.getReceptionOrder(formGroup) as any;

        expect(receptionOrder).toMatchObject(sampleWithNewData);
      });

      it('should return NewReceptionOrder for empty ReceptionOrder initial value', () => {
        const formGroup = service.createReceptionOrderFormGroup();

        const receptionOrder = service.getReceptionOrder(formGroup) as any;

        expect(receptionOrder).toMatchObject({});
      });

      it('should return IReceptionOrder', () => {
        const formGroup = service.createReceptionOrderFormGroup(sampleWithRequiredData);

        const receptionOrder = service.getReceptionOrder(formGroup) as any;

        expect(receptionOrder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReceptionOrder should not enable id FormControl', () => {
        const formGroup = service.createReceptionOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReceptionOrder should disable id FormControl', () => {
        const formGroup = service.createReceptionOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
