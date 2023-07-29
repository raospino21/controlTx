import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../data-sheet-interface.test-samples';

import { DataSheetInterfaceFormService } from './data-sheet-interface-form.service';

describe('DataSheetInterface Form Service', () => {
  let service: DataSheetInterfaceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DataSheetInterfaceFormService);
  });

  describe('Service methods', () => {
    describe('createDataSheetInterfaceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDataSheetInterfaceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            colcircuitosLotNumber: expect.any(Object),
            orderAmount: expect.any(Object),
            amountReceived: expect.any(Object),
            remission: expect.any(Object),
            entryDate: expect.any(Object),
            iesOrderNumber: expect.any(Object),
          })
        );
      });

      it('passing IDataSheetInterface should create a new form with FormGroup', () => {
        const formGroup = service.createDataSheetInterfaceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            colcircuitosLotNumber: expect.any(Object),
            orderAmount: expect.any(Object),
            amountReceived: expect.any(Object),
            remission: expect.any(Object),
            entryDate: expect.any(Object),
            iesOrderNumber: expect.any(Object),
          })
        );
      });
    });

    describe('getDataSheetInterface', () => {
      it('should return NewDataSheetInterface for default DataSheetInterface initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDataSheetInterfaceFormGroup(sampleWithNewData);

        const dataSheetInterface = service.getDataSheetInterface(formGroup) as any;

        expect(dataSheetInterface).toMatchObject(sampleWithNewData);
      });

      it('should return NewDataSheetInterface for empty DataSheetInterface initial value', () => {
        const formGroup = service.createDataSheetInterfaceFormGroup();

        const dataSheetInterface = service.getDataSheetInterface(formGroup) as any;

        expect(dataSheetInterface).toMatchObject({});
      });

      it('should return IDataSheetInterface', () => {
        const formGroup = service.createDataSheetInterfaceFormGroup(sampleWithRequiredData);

        const dataSheetInterface = service.getDataSheetInterface(formGroup) as any;

        expect(dataSheetInterface).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDataSheetInterface should not enable id FormControl', () => {
        const formGroup = service.createDataSheetInterfaceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDataSheetInterface should disable id FormControl', () => {
        const formGroup = service.createDataSheetInterfaceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
