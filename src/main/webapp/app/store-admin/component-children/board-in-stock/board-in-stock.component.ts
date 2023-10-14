import { HttpHeaders } from '@angular/common/http';
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

  ngOnInit(): void {
    this.getBoardsInStock();
  }

  getBoardsInStock(): void {
    this.service.getBoardsInStock(this.page - 1, this.pageSize).subscribe(res => {
      this.onSuccess(res.body!, res.headers);
    });
  }

  private onSuccess(interfaceBoards: IInterfaceBoard[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
    this.interfaceBoards = interfaceBoards;
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
}
