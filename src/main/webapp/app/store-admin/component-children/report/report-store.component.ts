import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { IControlInterfaceBoard } from 'app/entities/control-interface-board/control-interface-board.model';
import { StoreService } from './report-store.service';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'jhi-report',
  templateUrl: './report-store.component.html',
  styleUrls: ['./report-store.component.scss'],
})
export class ReportStoreComponent implements OnInit {
  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    public accountService: AccountService,
    protected service: StoreService
  ) {}

  controlInterfaceBoards?: IControlInterfaceBoard[];
  pageSize: number = ITEMS_PER_PAGE;
  page = 1;
  totalItems = 0;

  canView(role: string[]) {
    return this.accountService.hasAnyAuthority(role);
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    const queryObject: any = {
      page: this.page - 1,
      size: this.pageSize,
      reference: this.filters.reference,
      mac: this.filters.mac,
    };

    this.service.getControlBoardsAvailable(queryObject).subscribe(res => {
      this.onSuccess(res.body!, res.headers);
    });
  }

  private onSuccess(controlInterfaceBoards: IControlInterfaceBoard[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
    this.controlInterfaceBoards = controlInterfaceBoards;
  }

  public cleanFilters(): void {
    this.filters.reference = null;
    this.filters.mac = null;
    this.loadData();
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.pageSize);
  }

  nextPage() {
    if (this.page < this.totalPages) {
      this.page++;
      this.loadData();
    }
  }

  prevPage() {
    if (this.page > 1) {
      this.page--;
      this.loadData();
    }
  }

  filters = {
    reference: null,
    mac: null,
  };

  public filter(): void {
    this.page = 1;
    this.loadData();
  }
}
