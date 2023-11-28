import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { NgbAccordion, NgbModal } from '@ng-bootstrap/ng-bootstrap';
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

  @ViewChild('acc') acc: NgbAccordion | undefined;

  purchaseOrderComplete?: IPurchaseOrderCompleteResponse[];
  receptionOrderListSelected?: IReceptionOrder[];
  interfaceBoards?: IInterfaceBoard[];
  pageSize: number = 10;
  pagePurchaseOrder = 1;
  totalItemsPurchaseOrder = 0;

  pageInterfaceBoard = 1;
  InterfaceBoardTotalItems = 0;

  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';
  public orderIesSelected?: string;
  orderReceptionSelected?: number;
  public totalOrderAmount?: number;
  public totalAmountReceived?: number;
  detailReceptionOrder?: string = '';
  isValidated: boolean = false;

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    const queryObject: any = {
      page: this.pagePurchaseOrder - 1,
      size: this.pageSize,
    };
    this.service.getPurchaseOrderComplete(queryObject).subscribe({
      next: (res: HttpResponse<IPurchaseOrderCompleteResponse[]>) => this.onSuccess(res.body!, res.headers),
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  private onSuccess(response: IPurchaseOrderCompleteResponse[], headers: HttpHeaders): void {
    this.totalItemsPurchaseOrder = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));

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

  public showReceptionOrder(receptionOrderId: number, orderIesSelected: number) {
    this.orderIesSelected = '' + orderIesSelected;
    this.receptionOrderListSelected = this.purchaseOrderComplete!.find(
      item => item.purchaseOrder!.id === receptionOrderId
    )?.receptionOrderList;

    this.receptionOrderListSelected!.length > 0 ? this.expandPanel3() : this.collapsePanel3();
  }

  private expandPanel3() {
    this.acc!.expand('panel3');
    this.acc!.collapse('panel4');
  }

  private collapsePanel3() {
    this.acc!.collapse('panel3');
  }

  private expandPanel4() {
    this.acc!.expand('panel4');
  }

  private collapsePanel4() {
    this.acc!.collapse('panel4');
  }

  public showTxAssociatedToReceptionOrder(receptionOrderId: number): void {
    this.isValidated = false;
    this.orderReceptionSelected = receptionOrderId;
    this.detailReceptionOrder = 'Cantidad de Tarjetas Asociadas:';
    this.loadMac(false);
    this.expandPanel4();
  }

  public showTxValidatedToReceptionOrder(receptionOrderId: number): void {
    this.isValidated = true;
    this.orderReceptionSelected = receptionOrderId;
    this.detailReceptionOrder = 'Cantidad de Tarjetas Validadas:';
    this.loadMac(true);
    this.expandPanel4();
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

  get totalPagesPurchaseOrder(): number {
    return Math.ceil(this.totalItemsPurchaseOrder / this.pageSize);
  }

  nextPagePurchaseOrder() {
    if (this.pagePurchaseOrder < this.totalPagesPurchaseOrder) {
      this.pagePurchaseOrder++;
      this.load();
    }
  }

  prevPagePurchaseOrder() {
    if (this.pagePurchaseOrder > 1) {
      this.pagePurchaseOrder--;
      this.load();
    }
  }

  get totalPagesInterfaceBoard(): number {
    return Math.ceil(this.InterfaceBoardTotalItems / this.pageSize);
  }

  loadMac(isValidated: boolean): void {
    const queryObject: any = {
      page: this.pageInterfaceBoard - 1,
      size: this.pageSize,
      'receptionOrderId.equals': this.orderReceptionSelected,
    };
    isValidated ? this.fillOwnFilter(queryObject) : undefined;
    this.service.query(queryObject).subscribe({
      next: (res: HttpResponse<IInterfaceBoard[]>) => {
        this.InterfaceBoardTotalItems = Number(res.headers.get(TOTAL_COUNT_RESPONSE_HEADER));
        this.interfaceBoards = res.body!;
      },
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  protected fillOwnFilter(queryObject: any): any {
    queryObject['isValidated.equals'] = 'true';
  }

  changePageMac(direction: 'next' | 'prev'): void {
    const canChangePage = direction === 'next' ? this.pageInterfaceBoard < this.totalPagesInterfaceBoard : this.pageInterfaceBoard > 1;

    if (canChangePage) {
      this.pageInterfaceBoard += direction === 'next' ? 1 : -1;
      this.loadMac(false);
    }
  }

  nextPageMac(): void {
    this.changePageMac('next');
  }

  prevPageMac(): void {
    this.changePageMac('prev');
  }

  public prevPageIntPurchaseOrder() {
    if (this.pagePurchaseOrder > 1) {
      this.pagePurchaseOrder--;
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

  public downloadAssociatedBoardsReceptionOrder(): void {
    this.service.downloadAssociatedBoardsReceptionOrder(this.isValidated, this.orderReceptionSelected!).subscribe({
      next: (result: HttpResponse<Blob>) => {
        const bodyResponse = result.body as Blob;

        const link = document.createElement('a');
        link.href = URL.createObjectURL(bodyResponse);
        link.download = 'Tarjetas.csv';
        link.click();

        URL.revokeObjectURL(link.href);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
        console.log(error.error.detail);
        alert('Error al descargar el archivo');
      },
    });
  }
}
