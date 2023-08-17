import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReceptionOrderFormService } from './reception-order-form.service';
import { ReceptionOrderService } from '../service/reception-order.service';
import { IReceptionOrder } from '../reception-order.model';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';

import { ReceptionOrderUpdateComponent } from './reception-order-update.component';

describe('ReceptionOrder Management Update Component', () => {
  let comp: ReceptionOrderUpdateComponent;
  let fixture: ComponentFixture<ReceptionOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let receptionOrderFormService: ReceptionOrderFormService;
  let receptionOrderService: ReceptionOrderService;
  let purchaseOrderService: PurchaseOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReceptionOrderUpdateComponent],
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
      .overrideTemplate(ReceptionOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReceptionOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    receptionOrderFormService = TestBed.inject(ReceptionOrderFormService);
    receptionOrderService = TestBed.inject(ReceptionOrderService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PurchaseOrder query and add missing value', () => {
      const receptionOrder: IReceptionOrder = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 46477 };
      receptionOrder.purchaseOrder = purchaseOrder;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 93187 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [purchaseOrder];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receptionOrder });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders.map(expect.objectContaining)
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const receptionOrder: IReceptionOrder = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 69749 };
      receptionOrder.purchaseOrder = purchaseOrder;

      activatedRoute.data = of({ receptionOrder });
      comp.ngOnInit();

      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrder);
      expect(comp.receptionOrder).toEqual(receptionOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceptionOrder>>();
      const receptionOrder = { id: 123 };
      jest.spyOn(receptionOrderFormService, 'getReceptionOrder').mockReturnValue(receptionOrder);
      jest.spyOn(receptionOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receptionOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: receptionOrder }));
      saveSubject.complete();

      // THEN
      expect(receptionOrderFormService.getReceptionOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(receptionOrderService.update).toHaveBeenCalledWith(expect.objectContaining(receptionOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceptionOrder>>();
      const receptionOrder = { id: 123 };
      jest.spyOn(receptionOrderFormService, 'getReceptionOrder').mockReturnValue({ id: null });
      jest.spyOn(receptionOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receptionOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: receptionOrder }));
      saveSubject.complete();

      // THEN
      expect(receptionOrderFormService.getReceptionOrder).toHaveBeenCalled();
      expect(receptionOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReceptionOrder>>();
      const receptionOrder = { id: 123 };
      jest.spyOn(receptionOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receptionOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(receptionOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePurchaseOrder', () => {
      it('Should forward to purchaseOrderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(purchaseOrderService, 'comparePurchaseOrder');
        comp.comparePurchaseOrder(entity, entity2);
        expect(purchaseOrderService.comparePurchaseOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
