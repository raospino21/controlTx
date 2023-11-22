import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { StoreService } from '../store.service';
import { IBoardDetailsInSotck } from './board-details-in-sotck.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-board-in-stock',
  templateUrl: './board-in-stock.component.html',
  styleUrls: ['./board-in-stock.component.scss'],
})
export class BoardInStockComponent implements OnInit {
  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected service: StoreService,
    private modalService: NgbModal
  ) {}

  interfaceBoards?: IInterfaceBoard[];
  pageSize: number = ITEMS_PER_PAGE;
  boardDetailInStock?: IBoardDetailsInSotck;
  page = 1;
  totalItems = 0;

  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';

  ngOnInit(): void {
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

  downloadBoardsInStock() {
    this.service.downloadBoardsInStock().subscribe({
      next: (result: HttpResponse<Blob>) => {
        const bodyResponse = result.body as Blob;

        const link = document.createElement('a');
        link.href = URL.createObjectURL(bodyResponse);
        link.download = 'TarjetaEnStock.csv';
        link.click();

        URL.revokeObjectURL(link.href);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
        alert('Error al descargar las MACs');
      },
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
    this.showAlert('success', 'Exito!', 0);
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
    if (this.containsFilter()) {
      this.filter.mac = null;
      this.getBoardsInStock();
    }
  }

  private openModal(modal: any): void {
    this.modalService.open(modal, {
      keyboard: false,
    });
  }

  viewDetails(boardDetailsInSotckTpl: any): void {
    this.service.getBoardDetailsInSotck().subscribe({
      next: (res: HttpResponse<IBoardDetailsInSotck>) => {
        this.boardDetailInStock = res.body!;
        this.openModal(boardDetailsInSotckTpl);
      },
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  public containsFilter(): boolean {
    return this.filter.mac !== null && this.filter.mac !== '';
  }

  filter = {
    mac: null,
  };
}
