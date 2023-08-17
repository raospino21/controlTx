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
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { ReceptionOrderService } from 'app/entities/reception-order/service/reception-order.service';

import { InterfaceBoardUpdateComponent } from './interface-board-update.component';

describe('InterfaceBoard Management Update Component', () => {
  let comp: InterfaceBoardUpdateComponent;
  let fixture: ComponentFixture<InterfaceBoardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let interfaceBoardFormService: InterfaceBoardFormService;
  let interfaceBoardService: InterfaceBoardService;
  let receptionOrderService: ReceptionOrderService;

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
    receptionOrderService = TestBed.inject(ReceptionOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ReceptionOrder query and add missing value', () => {
      const interfaceBoard: IInterfaceBoard = { id: 456 };
      const receptionOrder: IReceptionOrder = { id: 54734 };
      interfaceBoard.receptionOrder = receptionOrder;

      const receptionOrderCollection: IReceptionOrder[] = [{ id: 7994 }];
      jest.spyOn(receptionOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: receptionOrderCollection })));
      const additionalReceptionOrders = [receptionOrder];
      const expectedCollection: IReceptionOrder[] = [...additionalReceptionOrders, ...receptionOrderCollection];
      jest.spyOn(receptionOrderService, 'addReceptionOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      expect(receptionOrderService.query).toHaveBeenCalled();
      expect(receptionOrderService.addReceptionOrderToCollectionIfMissing).toHaveBeenCalledWith(
        receptionOrderCollection,
        ...additionalReceptionOrders.map(expect.objectContaining)
      );
      expect(comp.receptionOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const interfaceBoard: IInterfaceBoard = { id: 456 };
      const receptionOrder: IReceptionOrder = { id: 34141 };
      interfaceBoard.receptionOrder = receptionOrder;

      activatedRoute.data = of({ interfaceBoard });
      comp.ngOnInit();

      expect(comp.receptionOrdersSharedCollection).toContain(receptionOrder);
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
    describe('compareReceptionOrder', () => {
      it('Should forward to receptionOrderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(receptionOrderService, 'compareReceptionOrder');
        comp.compareReceptionOrder(entity, entity2);
        expect(receptionOrderService.compareReceptionOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
