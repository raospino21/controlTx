import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInterfaceBoard } from '../interface-board.model';
import { InterfaceBoardService } from '../service/interface-board.service';
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { ReceptionOrderService } from 'app/entities/reception-order/service/reception-order.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CreateBoard, ICreateBoard } from './create-board.model';
import { IRequestStatus } from 'app/shared/request-status.model';

@Component({
  selector: 'jhi-interface-board-create',
  templateUrl: './interface-board-create.component.html',
})
export class InterfaceBoardCreateComponent implements OnInit {
  isSaving = false;
  interfaceBoard: IInterfaceBoard | null = null;
  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';

  receptionOrdersSharedCollection: IReceptionOrder[] = [];

  constructor(
    protected interfaceBoardService: InterfaceBoardService,
    protected receptionOrderService: ReceptionOrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadRelationshipsOptions();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    const createBoard = this.createModelFromForm();

    this.interfaceBoardService.createBoard(createBoard).subscribe({
      next: (res: IRequestStatus) => {
        this.showAlert('success', res.msg!, 1000);
      },
      error: (error: HttpErrorResponse) => this.showAlert('danger', error.error.title, 4000),
    });
  }

  public showAlert(typeAlertErrorMsg: string, errorMsg: string, showTime: number): void {
    this.typeAlertErrorMsg = typeAlertErrorMsg;
    this.errorMsg = errorMsg!;

    setTimeout(() => {
      this.errorMsg = '';
      this.previousState();
    }, showTime);
  }

  createModelFromForm(): ICreateBoard {
    return {
      ...new CreateBoard(),
      receptionOrder: this.formCreateInterfaceBoard.get(['receptionOrderOption'])!.value as IReceptionOrder,
      macs: [this.formCreateInterfaceBoard.get(['mac'])?.value.trim()],
    };
  }

  formCreateInterfaceBoard = this.fb.group({
    receptionOrderOption: [null, [Validators.required]],
    mac: [null, [Validators.required]],
  });

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected loadRelationshipsOptions(): void {
    const queryObject: any = {
      page: 0,
      size: 30,
      eagerload: true,
    };

    this.receptionOrderService
      .receptionOrderAvailable(queryObject)
      .pipe(map((res: HttpResponse<IReceptionOrder[]>) => res.body ?? []))
      .pipe(
        map((receptionOrders: IReceptionOrder[]) =>
          this.receptionOrderService.addReceptionOrderToCollectionIfMissing<IReceptionOrder>(
            receptionOrders,
            this.interfaceBoard?.receptionOrder
          )
        )
      )
      .subscribe((receptionOrders: IReceptionOrder[]) => {
        this.receptionOrdersSharedCollection = receptionOrders;
      });
  }
}
