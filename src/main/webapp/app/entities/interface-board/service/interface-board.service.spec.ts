import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInterfaceBoard } from '../interface-board.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../interface-board.test-samples';

import { InterfaceBoardService } from './interface-board.service';

const requireRestSample: IInterfaceBoard = {
  ...sampleWithRequiredData,
};

describe('InterfaceBoard Service', () => {
  let service: InterfaceBoardService;
  let httpMock: HttpTestingController;
  let expectedResult: IInterfaceBoard | IInterfaceBoard[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InterfaceBoardService);
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

    it('should create a InterfaceBoard', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const interfaceBoard = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(interfaceBoard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InterfaceBoard', () => {
      const interfaceBoard = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(interfaceBoard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InterfaceBoard', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InterfaceBoard', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InterfaceBoard', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInterfaceBoardToCollectionIfMissing', () => {
      it('should add a InterfaceBoard to an empty array', () => {
        const interfaceBoard: IInterfaceBoard = sampleWithRequiredData;
        expectedResult = service.addInterfaceBoardToCollectionIfMissing([], interfaceBoard);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interfaceBoard);
      });

      it('should not add a InterfaceBoard to an array that contains it', () => {
        const interfaceBoard: IInterfaceBoard = sampleWithRequiredData;
        const interfaceBoardCollection: IInterfaceBoard[] = [
          {
            ...interfaceBoard,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInterfaceBoardToCollectionIfMissing(interfaceBoardCollection, interfaceBoard);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InterfaceBoard to an array that doesn't contain it", () => {
        const interfaceBoard: IInterfaceBoard = sampleWithRequiredData;
        const interfaceBoardCollection: IInterfaceBoard[] = [sampleWithPartialData];
        expectedResult = service.addInterfaceBoardToCollectionIfMissing(interfaceBoardCollection, interfaceBoard);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interfaceBoard);
      });

      it('should add only unique InterfaceBoard to an array', () => {
        const interfaceBoardArray: IInterfaceBoard[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const interfaceBoardCollection: IInterfaceBoard[] = [sampleWithRequiredData];
        expectedResult = service.addInterfaceBoardToCollectionIfMissing(interfaceBoardCollection, ...interfaceBoardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const interfaceBoard: IInterfaceBoard = sampleWithRequiredData;
        const interfaceBoard2: IInterfaceBoard = sampleWithPartialData;
        expectedResult = service.addInterfaceBoardToCollectionIfMissing([], interfaceBoard, interfaceBoard2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interfaceBoard);
        expect(expectedResult).toContain(interfaceBoard2);
      });

      it('should accept null and undefined values', () => {
        const interfaceBoard: IInterfaceBoard = sampleWithRequiredData;
        expectedResult = service.addInterfaceBoardToCollectionIfMissing([], null, interfaceBoard, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interfaceBoard);
      });

      it('should return initial array if no InterfaceBoard is added', () => {
        const interfaceBoardCollection: IInterfaceBoard[] = [sampleWithRequiredData];
        expectedResult = service.addInterfaceBoardToCollectionIfMissing(interfaceBoardCollection, undefined, null);
        expect(expectedResult).toEqual(interfaceBoardCollection);
      });
    });

    describe('compareInterfaceBoard', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInterfaceBoard(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInterfaceBoard(entity1, entity2);
        const compareResult2 = service.compareInterfaceBoard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInterfaceBoard(entity1, entity2);
        const compareResult2 = service.compareInterfaceBoard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInterfaceBoard(entity1, entity2);
        const compareResult2 = service.compareInterfaceBoard(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
