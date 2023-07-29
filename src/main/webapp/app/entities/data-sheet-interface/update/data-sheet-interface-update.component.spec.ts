import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DataSheetInterfaceFormService } from './data-sheet-interface-form.service';
import { DataSheetInterfaceService } from '../service/data-sheet-interface.service';
import { IDataSheetInterface } from '../data-sheet-interface.model';

import { DataSheetInterfaceUpdateComponent } from './data-sheet-interface-update.component';

describe('DataSheetInterface Management Update Component', () => {
  let comp: DataSheetInterfaceUpdateComponent;
  let fixture: ComponentFixture<DataSheetInterfaceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dataSheetInterfaceFormService: DataSheetInterfaceFormService;
  let dataSheetInterfaceService: DataSheetInterfaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DataSheetInterfaceUpdateComponent],
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
      .overrideTemplate(DataSheetInterfaceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DataSheetInterfaceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dataSheetInterfaceFormService = TestBed.inject(DataSheetInterfaceFormService);
    dataSheetInterfaceService = TestBed.inject(DataSheetInterfaceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dataSheetInterface: IDataSheetInterface = { id: 456 };

      activatedRoute.data = of({ dataSheetInterface });
      comp.ngOnInit();

      expect(comp.dataSheetInterface).toEqual(dataSheetInterface);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDataSheetInterface>>();
      const dataSheetInterface = { id: 123 };
      jest.spyOn(dataSheetInterfaceFormService, 'getDataSheetInterface').mockReturnValue(dataSheetInterface);
      jest.spyOn(dataSheetInterfaceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dataSheetInterface });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dataSheetInterface }));
      saveSubject.complete();

      // THEN
      expect(dataSheetInterfaceFormService.getDataSheetInterface).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dataSheetInterfaceService.update).toHaveBeenCalledWith(expect.objectContaining(dataSheetInterface));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDataSheetInterface>>();
      const dataSheetInterface = { id: 123 };
      jest.spyOn(dataSheetInterfaceFormService, 'getDataSheetInterface').mockReturnValue({ id: null });
      jest.spyOn(dataSheetInterfaceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dataSheetInterface: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dataSheetInterface }));
      saveSubject.complete();

      // THEN
      expect(dataSheetInterfaceFormService.getDataSheetInterface).toHaveBeenCalled();
      expect(dataSheetInterfaceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDataSheetInterface>>();
      const dataSheetInterface = { id: 123 };
      jest.spyOn(dataSheetInterfaceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dataSheetInterface });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dataSheetInterfaceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
