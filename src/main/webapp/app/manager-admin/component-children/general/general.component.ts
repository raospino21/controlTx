import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { NgbAccordion, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ManagerGeneralService } from './manager-general.service';
import { IBrand } from 'app/entities/brand/brand.model';
import { IBrandCompleteInfo } from './brand-complete-info.model';
import { IOperatorCompleteInfo } from './operator-complete-info.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { IOperator } from 'app/entities/operator/operator.model';
import { IOperatorSelected, OperatorSelected } from './operator-selected.model';

@Component({
  selector: 'jhi-manager-general',
  templateUrl: './general.component.html',
  styleUrls: ['./general.component.scss'],
})
export class ManagerGeneralComponent implements OnInit {
  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected service: ManagerGeneralService,
    private modalService: NgbModal
  ) {}

  @ViewChild('acc') acc: NgbAccordion | undefined;
  pageSize: number = 10;
  page = 1;
  totalItems = 0;
  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';
  brandsCompleteInfo?: IBrandCompleteInfo[];
  operators?: IOperatorCompleteInfo[];
  interfaceBoards?: IInterfaceBoard[];
  operatorSelected?: IOperatorSelected;
  ngOnInit(): void {
    this.load();
  }

  load(): void {
    const queryObject: any = {
      page: this.page - 1,
      size: this.pageSize,
    };

    this.service.getBrands(queryObject).subscribe({
      next: (res: HttpResponse<IBrandCompleteInfo[]>) => this.onSuccess(res, res.headers),
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  private onSuccess(response: HttpResponse<IBrandCompleteInfo[]>, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
    this.brandsCompleteInfo = response.body!;
    this.showAlert('success', 'Exito!', 2000);
  }

  calculateTotalAmount(receptionOrderList: IReceptionOrder[]): number {
    return receptionOrderList?.reduce((accumulator, receptionOrder) => {
      return accumulator + receptionOrder.amountReceived!;
    }, 0);
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

  showOperatorsBtn(brand: IBrand): void {
    const queryObject: any = {
      'brandId.equals': brand.id,
    };
    this.service.getOperators(queryObject).subscribe({
      next: (res: HttpResponse<IOperatorCompleteInfo[]>) => this.onSuccessOperators(res, res.headers),
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  private onSuccessOperators(response: HttpResponse<IOperatorCompleteInfo[]>, headers: HttpHeaders): void {
    this.operators = response.body!;
    this.togglePanel2(this.operators);
    this.showAlert('success', 'Exito!', 2000);
  }

  private togglePanel2(operators: IOperatorCompleteInfo[]) {
    if (this.hasValidOperators(operators)) {
      this.expandPanel2();
    } else {
      this.collapsePanel2();
    }
  }

  private hasValidOperators(operators: IOperatorCompleteInfo[]): boolean {
    return operators.some(operator => {
      return operator.contractSubList?.length! > 0;
    });
  }

  private expandPanel2() {
    this.acc!.expand('panel2');
  }

  private collapsePanel2() {
    this.acc!.collapse('panel2');
  }

  loadMac(operator?: IOperator, reference?: string, type?: ContractType): void {
    this.operatorSelected = new OperatorSelected(operator, type, reference);
    const queryObject: any = {
      page: this.page - 1,
      size: this.pageSize,
    };

    this.service.getMacByContracTypeReference(reference, type).subscribe({
      next: (res: HttpResponse<IInterfaceBoard[]>) => this.onSuccessMacByContracType(res, res.headers),
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  private onSuccessMacByContracType(response: HttpResponse<IInterfaceBoard[]>, headers: HttpHeaders): void {
    this.interfaceBoards = response.body!;
    this.togglePanel3(this.interfaceBoards);
    this.showAlert('success', 'Exito!', 2000);
  }

  private togglePanel3(interfaceBoards: IInterfaceBoard[]) {
    if (this.hasValidInterfaceBoard(interfaceBoards)) {
      this.expandPanel3();
    } else {
      this.collapsePanel3();
    }
  }

  private hasValidInterfaceBoard(interfaceBoards: IInterfaceBoard[]): boolean {
    return interfaceBoards.length > 0;
  }

  private expandPanel3() {
    this.acc!.expand('panel3');
  }

  private collapsePanel3() {
    this.acc!.collapse('panel3');
  }
}
