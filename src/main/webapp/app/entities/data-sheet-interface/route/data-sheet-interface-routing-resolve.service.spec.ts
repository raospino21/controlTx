import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDataSheetInterface } from '../data-sheet-interface.model';
import { DataSheetInterfaceService } from '../service/data-sheet-interface.service';

import { DataSheetInterfaceRoutingResolveService } from './data-sheet-interface-routing-resolve.service';

describe('DataSheetInterface routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DataSheetInterfaceRoutingResolveService;
  let service: DataSheetInterfaceService;
  let resultDataSheetInterface: IDataSheetInterface | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(DataSheetInterfaceRoutingResolveService);
    service = TestBed.inject(DataSheetInterfaceService);
    resultDataSheetInterface = undefined;
  });

  describe('resolve', () => {
    it('should return IDataSheetInterface returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDataSheetInterface = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDataSheetInterface).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDataSheetInterface = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDataSheetInterface).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IDataSheetInterface>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDataSheetInterface = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDataSheetInterface).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
