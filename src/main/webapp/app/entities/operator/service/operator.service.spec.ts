import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOperator } from '../operator.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../operator.test-samples';

import { OperatorService } from './operator.service';

const requireRestSample: IOperator = {
  ...sampleWithRequiredData,
};

describe('Operator Service', () => {
  let service: OperatorService;
  let httpMock: HttpTestingController;
  let expectedResult: IOperator | IOperator[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperatorService);
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

    it('should create a Operator', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const operator = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(operator).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Operator', () => {
      const operator = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(operator).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Operator', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Operator', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Operator', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOperatorToCollectionIfMissing', () => {
      it('should add a Operator to an empty array', () => {
        const operator: IOperator = sampleWithRequiredData;
        expectedResult = service.addOperatorToCollectionIfMissing([], operator);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operator);
      });

      it('should not add a Operator to an array that contains it', () => {
        const operator: IOperator = sampleWithRequiredData;
        const operatorCollection: IOperator[] = [
          {
            ...operator,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOperatorToCollectionIfMissing(operatorCollection, operator);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Operator to an array that doesn't contain it", () => {
        const operator: IOperator = sampleWithRequiredData;
        const operatorCollection: IOperator[] = [sampleWithPartialData];
        expectedResult = service.addOperatorToCollectionIfMissing(operatorCollection, operator);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operator);
      });

      it('should add only unique Operator to an array', () => {
        const operatorArray: IOperator[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const operatorCollection: IOperator[] = [sampleWithRequiredData];
        expectedResult = service.addOperatorToCollectionIfMissing(operatorCollection, ...operatorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operator: IOperator = sampleWithRequiredData;
        const operator2: IOperator = sampleWithPartialData;
        expectedResult = service.addOperatorToCollectionIfMissing([], operator, operator2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operator);
        expect(expectedResult).toContain(operator2);
      });

      it('should accept null and undefined values', () => {
        const operator: IOperator = sampleWithRequiredData;
        expectedResult = service.addOperatorToCollectionIfMissing([], null, operator, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operator);
      });

      it('should return initial array if no Operator is added', () => {
        const operatorCollection: IOperator[] = [sampleWithRequiredData];
        expectedResult = service.addOperatorToCollectionIfMissing(operatorCollection, undefined, null);
        expect(expectedResult).toEqual(operatorCollection);
      });
    });

    describe('compareOperator', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOperator(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOperator(entity1, entity2);
        const compareResult2 = service.compareOperator(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOperator(entity1, entity2);
        const compareResult2 = service.compareOperator(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOperator(entity1, entity2);
        const compareResult2 = service.compareOperator(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
