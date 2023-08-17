import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReceptionOrder } from '../reception-order.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../reception-order.test-samples';

import { ReceptionOrderService, RestReceptionOrder } from './reception-order.service';

const requireRestSample: RestReceptionOrder = {
  ...sampleWithRequiredData,
  entryDate: sampleWithRequiredData.entryDate?.toJSON(),
  warrantyDate: sampleWithRequiredData.warrantyDate?.toJSON(),
};

describe('ReceptionOrder Service', () => {
  let service: ReceptionOrderService;
  let httpMock: HttpTestingController;
  let expectedResult: IReceptionOrder | IReceptionOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReceptionOrderService);
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

    it('should create a ReceptionOrder', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const receptionOrder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(receptionOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReceptionOrder', () => {
      const receptionOrder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(receptionOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReceptionOrder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReceptionOrder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReceptionOrder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReceptionOrderToCollectionIfMissing', () => {
      it('should add a ReceptionOrder to an empty array', () => {
        const receptionOrder: IReceptionOrder = sampleWithRequiredData;
        expectedResult = service.addReceptionOrderToCollectionIfMissing([], receptionOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receptionOrder);
      });

      it('should not add a ReceptionOrder to an array that contains it', () => {
        const receptionOrder: IReceptionOrder = sampleWithRequiredData;
        const receptionOrderCollection: IReceptionOrder[] = [
          {
            ...receptionOrder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReceptionOrderToCollectionIfMissing(receptionOrderCollection, receptionOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReceptionOrder to an array that doesn't contain it", () => {
        const receptionOrder: IReceptionOrder = sampleWithRequiredData;
        const receptionOrderCollection: IReceptionOrder[] = [sampleWithPartialData];
        expectedResult = service.addReceptionOrderToCollectionIfMissing(receptionOrderCollection, receptionOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receptionOrder);
      });

      it('should add only unique ReceptionOrder to an array', () => {
        const receptionOrderArray: IReceptionOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const receptionOrderCollection: IReceptionOrder[] = [sampleWithRequiredData];
        expectedResult = service.addReceptionOrderToCollectionIfMissing(receptionOrderCollection, ...receptionOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const receptionOrder: IReceptionOrder = sampleWithRequiredData;
        const receptionOrder2: IReceptionOrder = sampleWithPartialData;
        expectedResult = service.addReceptionOrderToCollectionIfMissing([], receptionOrder, receptionOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receptionOrder);
        expect(expectedResult).toContain(receptionOrder2);
      });

      it('should accept null and undefined values', () => {
        const receptionOrder: IReceptionOrder = sampleWithRequiredData;
        expectedResult = service.addReceptionOrderToCollectionIfMissing([], null, receptionOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receptionOrder);
      });

      it('should return initial array if no ReceptionOrder is added', () => {
        const receptionOrderCollection: IReceptionOrder[] = [sampleWithRequiredData];
        expectedResult = service.addReceptionOrderToCollectionIfMissing(receptionOrderCollection, undefined, null);
        expect(expectedResult).toEqual(receptionOrderCollection);
      });
    });

    describe('compareReceptionOrder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReceptionOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReceptionOrder(entity1, entity2);
        const compareResult2 = service.compareReceptionOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReceptionOrder(entity1, entity2);
        const compareResult2 = service.compareReceptionOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReceptionOrder(entity1, entity2);
        const compareResult2 = service.compareReceptionOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
