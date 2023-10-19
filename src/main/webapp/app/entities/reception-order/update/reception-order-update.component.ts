import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ReceptionOrderFormService, ReceptionOrderFormGroup } from './reception-order-form.service';
import { IReceptionOrder } from '../reception-order.model';
import { ReceptionOrderService } from '../service/reception-order.service';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';

@Component({
  selector: 'jhi-reception-order-update',
  templateUrl: './reception-order-update.component.html',
})
export class ReceptionOrderUpdateComponent implements OnInit {
  isSaving = false;
  receptionOrder: IReceptionOrder | null = null;

  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];

  orderAmount: number = 0;

  editForm: ReceptionOrderFormGroup = this.receptionOrderFormService.createReceptionOrderFormGroup();

  constructor(
    protected receptionOrderService: ReceptionOrderService,
    protected receptionOrderFormService: ReceptionOrderFormService,
    protected purchaseOrderService: PurchaseOrderService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePurchaseOrder = (o1: IPurchaseOrder | null, o2: IPurchaseOrder | null): boolean =>
    this.purchaseOrderService.comparePurchaseOrder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receptionOrder }) => {
      this.receptionOrder = receptionOrder;
      if (receptionOrder) {
        this.updateForm(receptionOrder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const receptionOrder = this.receptionOrderFormService.getReceptionOrder(this.editForm);
    if (receptionOrder.id !== null) {
      this.subscribeToSaveResponse(this.receptionOrderService.update(receptionOrder));
    } else {
      this.subscribeToSaveResponse(this.receptionOrderService.create(receptionOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReceptionOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(receptionOrder: IReceptionOrder): void {
    this.receptionOrder = receptionOrder;
    this.receptionOrderFormService.resetForm(this.editForm, receptionOrder);

    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing<IPurchaseOrder>(
      this.purchaseOrdersSharedCollection,
      receptionOrder.purchaseOrder
    );
  }

  protected loadRelationshipsOptions(): void {
    this.purchaseOrderService
      .purchaseOrderAvailable()
      .pipe(map((res: HttpResponse<IPurchaseOrder[]>) => res.body ?? []))
      .pipe(
        map((purchaseOrders: IPurchaseOrder[]) =>
          this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing<IPurchaseOrder>(
            purchaseOrders,
            this.receptionOrder?.purchaseOrder
          )
        )
      )
      .subscribe((purchaseOrders: IPurchaseOrder[]) => (this.purchaseOrdersSharedCollection = purchaseOrders));
  }

  onSelectOption() {
    const selected = this.editForm.get('purchaseOrder')!.value as IPurchaseOrder;
    this.orderAmount = selected.orderAmount!;
  }
}
