import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { CommercialService } from './report-commercial.service';

@Component({
  selector: 'jhi-report',
  templateUrl: './report-commercial.component.html',
  styleUrls: ['./report-commercial.component.scss'],
})
export class ReportCommercialComponent implements OnInit {
  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected commercialService: CommercialService) {}

  interfaceBoards?: IInterfaceBoard[];
  pageSize: number = ITEMS_PER_PAGE;
  page = 1;
  totalItems = 0;

  ngOnInit(): void {
    this.getBoardsInStock();
  }

  getBoardsInStock(): void {
    this.commercialService.getBoardsInStock(this.page - 1, this.pageSize).subscribe(res => {
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
