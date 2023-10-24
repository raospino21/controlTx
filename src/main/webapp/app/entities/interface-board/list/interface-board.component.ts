import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, map, Observable, switchMap, tap } from 'rxjs';
import { NgbModal, NgbPanelChangeEvent } from '@ng-bootstrap/ng-bootstrap';

import { IInterfaceBoard } from '../interface-board.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, InterfaceBoardService } from '../service/interface-board.service';
import { InterfaceBoardDeleteDialogComponent } from '../delete/interface-board-delete-dialog.component';
import { FilterOptions, IFilterOptions, IFilterOption } from 'app/shared/filter/filter.model';
import { AccountService } from 'app/core/auth/account.service';
import { CreateBoard, ICreateBoard } from '../create/create-board.model';
import { FormBuilder, Validators } from '@angular/forms';
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { ReceptionOrderService } from 'app/entities/reception-order/service/reception-order.service';
import { UploadBoardComponent } from '../upload-board-file/upload-board.component';

@Component({
  selector: 'jhi-interface-board',
  templateUrl: './interface-board.component.html',
  styles: ['.min-width-300 { min-width: 800px; }'],
})
export class InterfaceBoardComponent implements OnInit {
  @ViewChild('createBoardModalTpl', { static: true }) createBoardModalTpl: TemplateRef<any> | undefined;
  interfaceBoards?: IInterfaceBoard[];
  interfaceBoard: IInterfaceBoard | null = null;
  receptionOrdersSharedCollection: IReceptionOrder[] = [];
  receptionOrderChild: IReceptionOrder | null = null;
  isLoading = false;
  predicate = 'id';
  ascending = true;
  filters: IFilterOptions = new FilterOptions();
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  createBoardFromFile: ICreateBoard | null = null;

  hidenButtonCreationBoardsByFile = false;

  constructor(
    protected interfaceBoardService: InterfaceBoardService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
    public accountService: AccountService,
    protected receptionOrderService: ReceptionOrderService,
    protected fb: FormBuilder
  ) {}

  trackId = (_index: number, item: IInterfaceBoard): number => this.interfaceBoardService.getInterfaceBoardIdentifier(item);

  ngOnInit(): void {
    console.log('----------- receptionOrder ngOnInit ', this.receptionOrderChild);
    this.load();
    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.predicate, this.ascending, filterOptions));
    this.loadRelationshipsOptions();
  }

  canView(role: string[]) {
    return this.accountService.hasAnyAuthority(role);
  }

  delete(interfaceBoard: IInterfaceBoard): void {
    const modalRef = this.modalService.open(InterfaceBoardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interfaceBoard = interfaceBoard;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending, this.filters.filterOptions))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.interfaceBoards = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IInterfaceBoard[] | null): IInterfaceBoard[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(
    page?: number,
    predicate?: string,
    ascending?: boolean,
    filterOptions?: IFilterOption[]
  ): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    filterOptions?.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    this.fillOwnFilter(queryObject);
    return this.interfaceBoardService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected fillOwnFilter(queryObject: any): any {
    if (this.filter.mac != null) {
      queryObject['mac.equals'] = this.filter.mac;
    }
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  filterBtn(): void {
    if (this.filter !== null && this.filter.mac != null) {
      const queryParamsObj: any = {
        mac: this.filter!.mac,
      };

      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    }
    this.load();
  }

  cleanFilters(): void {
    this.filter.mac = null;
    this.load();
  }

  filter = {
    mac: null,
  };

  public isActiveAccordion(): void {
    this.hidenButtonCreationBoardsByFile = true;
  }

  public beforeClose($event: NgbPanelChangeEvent): void {
    // if ($event.panelId === 'toggle-1' && $event.nextState === false) {
    //   $event.preventDefault();
    // }
  }

  public openModal(modal: any): void {
    this.modalService.open(modal, {
      backdrop: 'static',
      keyboard: false,
    });
  }

  createModelFromForm(): ICreateBoard {
    return {
      ...new CreateBoard(),
      receptionOrder: this.formCreateInterfaceBoard.get(['receptionOrderOption'])!.value as IReceptionOrder,
      macs: [this.formCreateInterfaceBoard.get(['mac'])?.value],
    };
  }

  formCreateInterfaceBoard = this.fb.group({
    receptionOrderOption: [null, [Validators.required]],
    mac: [null, [Validators.required]],
  });

  public cleanformCreateInterfaceBoard(): void {
    this.formCreateInterfaceBoard.patchValue({
      receptionOrderOption: null,
      mac: null,
    });
  }

  public createBoardsToCreateForFile(): void {
    // const bonus = this.createBonusFromForm();
    // this.infoBonusCreationForm = bonus;
    // this.resetBalanceAvailableInDiferentMonth();
    // this.setBalanceAvailableDiferentMonth(this.infoBonusCreationForm.startDate!);
    this.receptionOrderChild = this.createModelFromForm().receptionOrder!;
    console.log('----------- receptionOrder createBoardsToCreateForFile ', this.receptionOrderChild);

    this.modalService.dismissAll();
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
