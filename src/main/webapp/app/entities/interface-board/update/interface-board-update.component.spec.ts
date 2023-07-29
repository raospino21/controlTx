import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InterfaceBoardFormService } from './interface-board-form.service';
import { InterfaceBoardService } from '../service/interface-board.service';
import { IInterfaceBoard } from '../interface-board.model';
import { IDataSheetInterface } from 'app/entities/data-sheet-interface/data-sheet-interface.model';
import { DataSheetInterfaceService } from 'app/entities/data-sheet-interface/service/data-sheet-interface.service';

import { InterfaceBoardUpdateComponent } from './interface-board-update.component';

describe('InterfaceBoard Management Update Component', () => {
  let comp: InterfaceBoardUpdateComponent;
  let fixture: ComponentFixture<InterfaceBoardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let interfaceBoardFormService: InterfaceBoardFormService;
  let interfaceBoardService: InterfaceBoardService;
  let dataSheetInterfaceService: DataSheetInterfaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InterfaceBoardUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InterfaceBoardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InterfaceBoardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    interfaceBoardFormService = TestBed.inject(InterfaceBoardFormService);
    interfaceBoardService = TestBed.inject(InterfaceBoardService);
    dataSheetInterfaceService = TestBed.inject(DataSheetInterfaceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DataSheetInterface query and add missing value', () => {
      const interfaceBoard: IInterfaceBoard = { id: 456 };
      const dataSheetInterface: IDataSheetInterface = { id: 52293 };
      interfaceBoard.dataSheetInterface = dataSheetInterface;

      const dataSheetInterfaceCollection: IDataSheetInterface[] = [{ id: 54552 }];
      jest.spyOn(dataSheetInterfaceService, 'query').mockReturnValue(of(new HttpResponse({ body: dataSheetInterfaceCollection })));
      const additionalDataSheetInterfaces = [dataSheetInterface];
      const expectedCollection: IDataSheetInterface[] = [...additionalDataSheetInterfaces, ...dataSheetInterfaceCollection];
      jest.spyOn(dataSheetInterfaceService, 'addDataSheetInterfaceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      expect(dataSheetInterfaceService.query).toHaveBeenCalled();
      expect(dataSheetInterfaceService.addDataSheetInterfaceToCollectionIfMissing).toHaveBeenCalledWith(
        dataSheetInterfaceCollection,
        ...additionalDataSheetInterfaces.map(expect.objectContaining)
      );
      expect(comp.dataSheetInterfacesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const interfaceBoard: IInterfaceBoard = { id: 456 };
      const dataSheetInterface: IDataSheetInterface = { id: 44630 };
      interfaceBoard.dataSheetInterface = dataSheetInterface;

      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      expect(comp.dataSheetInterfacesSharedCollection).toContain(dataSheetInterface);
      expect(comp.interfaceBoard).toEqual(interfaceBoard);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterfaceBoard>>();
      const interfaceBoard = { id: 123 };
      jest.spyOn(interfaceBoardFormService, 'getInterfaceBoard').mockReturnValue(interfaceBoard);
      jest.spyOn(interfaceBoardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interfaceBoard }));
      saveSubject.complete();

      // THEN
      expect(interfaceBoardFormService.getInterfaceBoard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(interfaceBoardService.update).toHaveBeenCalledWith(expect.objectContaining(interfaceBoard));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterfaceBoard>>();
      const interfaceBoard = { id: 123 };
      jest.spyOn(interfaceBoardFormService, 'getInterfaceBoard').mockReturnValue({ id: null });
      jest.spyOn(interfaceBoardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interfaceBoard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: interfaceBoard }));
      saveSubject.complete();

      // THEN
      expect(interfaceBoardFormService.getInterfaceBoard).toHaveBeenCalled();
      expect(interfaceBoardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInterfaceBoard>>();
      const interfaceBoard = { id: 123 };
      jest.spyOn(interfaceBoardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(interfaceBoardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDataSheetInterface', () => {
      it('Should forward to dataSheetInterfaceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(dataSheetInterfaceService, 'compareDataSheetInterface');
        comp.compareDataSheetInterface(entity, entity2);
        expect(dataSheetInterfaceService.compareDataSheetInterface).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
