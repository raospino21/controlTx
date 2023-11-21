import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ManagerStoreService } from './manager-store.service';
import { IPurchaseOrderCompleteResponse } from './purchaseorder-complete-response.model';

@Component({
  selector: 'jhi-manager-store',
  templateUrl: './store.component.html',
})
export class ManagerStoreComponent implements OnInit {
  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected service: ManagerStoreService,
    private modalService: NgbModal
  ) {}

  purchaseOrderComplete?: IPurchaseOrderCompleteResponse[];
  receptionOrderListSelected?: IReceptionOrder[];
  pageSize: number = 10;
  page = 1;
  totalItems = 0;
  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';

  public totalOrderAmount?: number;
  public totalAmountReceived?: number;

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    const queryObject: any = {
      page: this.page - 1,
      size: this.pageSize,
    };
    this.service.getPurchaseOrderComplete(queryObject).subscribe({
      next: (res: HttpResponse<IPurchaseOrderCompleteResponse[]>) => this.onSuccess(res.body!, res.headers),
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  private onSuccess(response: IPurchaseOrderCompleteResponse[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));

    this.purchaseOrderComplete = response.map(item => {
      item.purchaseOrder!.amountReceived = this.calculateTotalAmount(item.receptionOrderList!);
      return item;
    });

    this.totalOrderAmount = this.purchaseOrderComplete.reduce((acumulador, objeto) => acumulador + objeto.purchaseOrder?.orderAmount!, 0);
    this.totalAmountReceived = this.purchaseOrderComplete.reduce(
      (acumulador, objeto) => acumulador + objeto.purchaseOrder?.amountReceived!,
      0
    );

    this.showAlert('success', 'Exito!', 20);
  }

  calculateTotalAmount(receptionOrderList: IReceptionOrder[]): number {
    return receptionOrderList?.reduce((accumulator, receptionOrder) => {
      return accumulator + receptionOrder.amountReceived!;
    }, 0);
  }

  public showReceptionOrder(receptionOrderId: number, modal: any) {
    this.receptionOrderListSelected = this.purchaseOrderComplete!.find(
      item => item.purchaseOrder!.id === receptionOrderId
    )?.receptionOrderList;
    this.openModal(modal);
  }

  private openModal(modal: any): void {
    this.modalService.open(modal, {
      keyboard: false,
      size: 'lg',
    });
  }

  private onError(error: HttpErrorResponse): void {
    this.showAlert('danger', error.error.detail, 4000);
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.pageSize);
  }

  nextPage() {
    if (this.page < this.totalPages) {
      this.page++;
      this.load();
    }
  }

  prevPage() {
    if (this.page > 1) {
      this.page--;
      this.load();
    }
  }

  public showAlert(typeAlertErrorMsg: string, errorMsg: string, showTime: number): void {
    this.typeAlertErrorMsg = typeAlertErrorMsg;
    this.errorMsg = errorMsg!;

    setTimeout(() => {
      this.errorMsg = '';
    }, showTime);
  }
}
