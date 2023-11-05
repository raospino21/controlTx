import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ManagerGeneralService } from './manager-general.service';
import { IBrand } from 'app/entities/brand/brand.model';
import { IBrandCompleteInfo } from './brand-complete-info.model';

@Component({
  selector: 'jhi-manager-general',
  templateUrl: './general.component.html',
})
export class ManagerGeneralComponent implements OnInit {
  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected service: ManagerGeneralService,
    private modalService: NgbModal
  ) {}

  pageSize: number = 10;
  page = 1;
  totalItems = 0;
  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';
  brandsCompleteInfo?: IBrandCompleteInfo[];

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
}
