import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDataSheetInterface } from '../data-sheet-interface.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../data-sheet-interface.test-samples';

import { DataSheetInterfaceService, RestDataSheetInterface } from './data-sheet-interface.service';

const requireRestSample: RestDataSheetInterface = {
  ...sampleWithRequiredData,
  entryDate: sampleWithRequiredData.entryDate?.toJSON(),
};

describe('DataSheetInterface Service', () => {
  let service: DataSheetInterfaceService;
  let httpMock: HttpTestingController;
  let expectedResult: IDataSheetInterface | IDataSheetInterface[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DataSheetInterfaceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a DataSheetInterface', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const dataSheetInterface = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dataSheetInterface).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DataSheetInterface', () => {
      const dataSheetInterface = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dataSheetInterface).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DataSheetInterface', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DataSheetInterface', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DataSheetInterface', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDataSheetInterfaceToCollectionIfMissing', () => {
      it('should add a DataSheetInterface to an empty array', () => {
        const dataSheetInterface: IDataSheetInterface = sampleWithRequiredData;
        expectedResult = service.addDataSheetInterfaceToCollectionIfMissing([], dataSheetInterface);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dataSheetInterface);
      });

      it('should not add a DataSheetInterface to an array that contains it', () => {
        const dataSheetInterface: IDataSheetInterface = sampleWithRequiredData;
        const dataSheetInterfaceCollection: IDataSheetInterface[] = [
          {
            ...dataSheetInterface,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDataSheetInterfaceToCollectionIfMissing(dataSheetInterfaceCollection, dataSheetInterface);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DataSheetInterface to an array that doesn't contain it", () => {
        const dataSheetInterface: IDataSheetInterface = sampleWithRequiredData;
        const dataSheetInterfaceCollection: IDataSheetInterface[] = [sampleWithPartialData];
        expectedResult = service.addDataSheetInterfaceToCollectionIfMissing(dataSheetInterfaceCollection, dataSheetInterface);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dataSheetInterface);
      });

      it('should add only unique DataSheetInterface to an array', () => {
        const dataSheetInterfaceArray: IDataSheetInterface[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dataSheetInterfaceCollection: IDataSheetInterface[] = [sampleWithRequiredData];
        expectedResult = service.addDataSheetInterfaceToCollectionIfMissing(dataSheetInterfaceCollection, ...dataSheetInterfaceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dataSheetInterface: IDataSheetInterface = sampleWithRequiredData;
        const dataSheetInterface2: IDataSheetInterface = sampleWithPartialData;
        expectedResult = service.addDataSheetInterfaceToCollectionIfMissing([], dataSheetInterface, dataSheetInterface2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dataSheetInterface);
        expect(expectedResult).toContain(dataSheetInterface2);
      });

      it('should accept null and undefined values', () => {
        const dataSheetInterface: IDataSheetInterface = sampleWithRequiredData;
        expectedResult = service.addDataSheetInterfaceToCollectionIfMissing([], null, dataSheetInterface, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dataSheetInterface);
      });

      it('should return initial array if no DataSheetInterface is added', () => {
        const dataSheetInterfaceCollection: IDataSheetInterface[] = [sampleWithRequiredData];
        expectedResult = service.addDataSheetInterfaceToCollectionIfMissing(dataSheetInterfaceCollection, undefined, null);
        expect(expectedResult).toEqual(dataSheetInterfaceCollection);
      });
    });

    describe('compareDataSheetInterface', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDataSheetInterface(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDataSheetInterface(entity1, entity2);
        const compareResult2 = service.compareDataSheetInterface(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDataSheetInterface(entity1, entity2);
        const compareResult2 = service.compareDataSheetInterface(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDataSheetInterface(entity1, entity2);
        const compareResult2 = service.compareDataSheetInterface(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
