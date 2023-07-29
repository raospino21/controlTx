import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IControlInterfaceBoard } from '../control-interface-board.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../control-interface-board.test-samples';

import { ControlInterfaceBoardService, RestControlInterfaceBoard } from './control-interface-board.service';

const requireRestSample: RestControlInterfaceBoard = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  finishTime: sampleWithRequiredData.finishTime?.toJSON(),
};

describe('ControlInterfaceBoard Service', () => {
  let service: ControlInterfaceBoardService;
  let httpMock: HttpTestingController;
  let expectedResult: IControlInterfaceBoard | IControlInterfaceBoard[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ControlInterfaceBoardService);
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

    it('should create a ControlInterfaceBoard', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const controlInterfaceBoard = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(controlInterfaceBoard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ControlInterfaceBoard', () => {
      const controlInterfaceBoard = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(controlInterfaceBoard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ControlInterfaceBoard', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ControlInterfaceBoard', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ControlInterfaceBoard', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addControlInterfaceBoardToCollectionIfMissing', () => {
      it('should add a ControlInterfaceBoard to an empty array', () => {
        const controlInterfaceBoard: IControlInterfaceBoard = sampleWithRequiredData;
        expectedResult = service.addControlInterfaceBoardToCollectionIfMissing([], controlInterfaceBoard);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(controlInterfaceBoard);
      });

      it('should not add a ControlInterfaceBoard to an array that contains it', () => {
        const controlInterfaceBoard: IControlInterfaceBoard = sampleWithRequiredData;
        const controlInterfaceBoardCollection: IControlInterfaceBoard[] = [
          {
            ...controlInterfaceBoard,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addControlInterfaceBoardToCollectionIfMissing(controlInterfaceBoardCollection, controlInterfaceBoard);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ControlInterfaceBoard to an array that doesn't contain it", () => {
        const controlInterfaceBoard: IControlInterfaceBoard = sampleWithRequiredData;
        const controlInterfaceBoardCollection: IControlInterfaceBoard[] = [sampleWithPartialData];
        expectedResult = service.addControlInterfaceBoardToCollectionIfMissing(controlInterfaceBoardCollection, controlInterfaceBoard);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(controlInterfaceBoard);
      });

      it('should add only unique ControlInterfaceBoard to an array', () => {
        const controlInterfaceBoardArray: IControlInterfaceBoard[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const controlInterfaceBoardCollection: IControlInterfaceBoard[] = [sampleWithRequiredData];
        expectedResult = service.addControlInterfaceBoardToCollectionIfMissing(
          controlInterfaceBoardCollection,
          ...controlInterfaceBoardArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const controlInterfaceBoard: IControlInterfaceBoard = sampleWithRequiredData;
        const controlInterfaceBoard2: IControlInterfaceBoard = sampleWithPartialData;
        expectedResult = service.addControlInterfaceBoardToCollectionIfMissing([], controlInterfaceBoard, controlInterfaceBoard2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(controlInterfaceBoard);
        expect(expectedResult).toContain(controlInterfaceBoard2);
      });

      it('should accept null and undefined values', () => {
        const controlInterfaceBoard: IControlInterfaceBoard = sampleWithRequiredData;
        expectedResult = service.addControlInterfaceBoardToCollectionIfMissing([], null, controlInterfaceBoard, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(controlInterfaceBoard);
      });

      it('should return initial array if no ControlInterfaceBoard is added', () => {
        const controlInterfaceBoardCollection: IControlInterfaceBoard[] = [sampleWithRequiredData];
        expectedResult = service.addControlInterfaceBoardToCollectionIfMissing(controlInterfaceBoardCollection, undefined, null);
        expect(expectedResult).toEqual(controlInterfaceBoardCollection);
      });
    });

    describe('compareControlInterfaceBoard', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareControlInterfaceBoard(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareControlInterfaceBoard(entity1, entity2);
        const compareResult2 = service.compareControlInterfaceBoard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareControlInterfaceBoard(entity1, entity2);
        const compareResult2 = service.compareControlInterfaceBoard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareControlInterfaceBoard(entity1, entity2);
        const compareResult2 = service.compareControlInterfaceBoard(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
