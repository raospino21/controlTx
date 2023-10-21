import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { StoreService } from '../store.service';

@Component({
  selector: 'jhi-report',
  templateUrl: './board-in-stock.component.html',
})
export class BoardInStockComponent implements OnInit {
  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected service: StoreService) {}

  interfaceBoards?: IInterfaceBoard[];
  pageSize: number = ITEMS_PER_PAGE;
  page = 1;
  totalItems = 0;

  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';

  ngOnInit(): void {
    console.log('.... init');

    this.getBoardsInStock();
  }

  getBoardsInStock(): void {
    const queryObject: any = {
      page: this.page - 1,
      size: this.pageSize,
      mac: this.filter!.mac,
    };
    this.service.getBoardsInStock(queryObject).subscribe({
      next: (res: HttpResponse<IInterfaceBoard[]>) => this.onSuccess(res.body!, res.headers),
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  protected fillOwnFilter(queryObject: any): any {
    if (this.filter.mac != null) {
      queryObject['mac.equals'] = this.filter.mac;
    }
  }

  private onSuccess(interfaceBoards: IInterfaceBoard[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
    this.interfaceBoards = interfaceBoards;
    this.showAlert('success', 'Exito!', 2000);
  }

  private onError(error: HttpErrorResponse): void {
    this.filter.mac = null;
    this.showAlert('danger', error.error.detail, 4000);
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.pageSize);
  }

  nextPage() {
    if (this.page < this.totalPages) {
      this.page++;
      this.getBoardsInStock();
    }
  }

  prevPage() {
    if (this.page > 1) {
      this.page--;
      this.getBoardsInStock();
    }
  }

  public showAlert(typeAlertErrorMsg: string, errorMsg: string, showTime: number): void {
    this.typeAlertErrorMsg = typeAlertErrorMsg;
    this.errorMsg = errorMsg!;

    setTimeout(() => {
      this.errorMsg = '';
    }, showTime);
  }

  cleanFilters(): void {
    this.filter.mac = null;
    this.getBoardsInStock();
  }

  filter = {
    mac: null,
  };
}
