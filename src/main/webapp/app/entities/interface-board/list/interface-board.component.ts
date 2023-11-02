import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { NgbModal, NgbPanelChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { Observable, combineLatest, filter, map, switchMap, tap } from 'rxjs';

import { IInterfaceBoard } from '../interface-board.model';

import { FormBuilder, Validators } from '@angular/forms';
import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { IReceptionOrder } from 'app/entities/reception-order/reception-order.model';
import { ReceptionOrderService } from 'app/entities/reception-order/service/reception-order.service';
import { FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter/filter.model';
import { IRequestStatus } from 'app/shared/request-status.model';
import { CreateBoard, ICreateBoard } from '../create/create-board.model';
import { InterfaceBoardDeleteDialogComponent } from '../delete/interface-board-delete-dialog.component';
import { EntityArrayResponseType, InterfaceBoardService } from '../service/interface-board.service';
import { UploadBoardService } from '../upload-board-file/upload-board.service';

@Component({
  selector: 'jhi-interface-board',
  templateUrl: './interface-board.component.html',
  styles: ['.min-width-300 { min-width: 800px; }'],
})
export class InterfaceBoardComponent implements OnInit {
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
  macs?: string[] = [];
  macsWithErros?: string[] = [];
  public quantityOfErrors = 0;
  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';

  constructor(
    protected interfaceBoardService: InterfaceBoardService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
    public accountService: AccountService,
    protected receptionOrderService: ReceptionOrderService,
    protected fb: FormBuilder,
    protected uploadService: UploadBoardService
  ) {}

  trackId = (_index: number, item: IInterfaceBoard): number => this.interfaceBoardService.getInterfaceBoardIdentifier(item);

  ngOnInit(): void {
    this.load();
    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.predicate, this.ascending, filterOptions));
    this.loadRelationshipsOptions();

    this.uploadService.layersMacs$().subscribe(layers => {
      this.resetData();
      this.macs = layers.macs;
    });
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

  update(interfaceBoard: IInterfaceBoard): void {
    interfaceBoard.isValidated = true;

    this.interfaceBoardService.update(interfaceBoard).subscribe({
      next: () => this.showAlert('success', 'success', 2000, false),
      error: (error: HttpErrorResponse) => this.showAlert('danger', error.error.title, 4000, false),
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
    if ($event.panelId === 'toggle-1' && $event.nextState === false) {
      $event.preventDefault();
    }
  }

  public openModal(modal: any): void {
    this.cleanformCreateInterfaceBoard();
    console.log('-------  orden de recepcion  ', this.formCreateInterfaceBoard.get(['receptionOrderOption'])!.value);
    console.log(' macs con errores ', this.macsWithErros);
    console.log(' macs con errores ', this.macs);

    this.modalService.open(modal, {
      backdrop: 'static',
      keyboard: false,
      size: 'lg',
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
    this.resetData();
    this.formCreateInterfaceBoard.patchValue({
      receptionOrderOption: null,
      mac: null,
    });
  }

  protected resetData() {
    this.quantityOfErrors = 0;
    this.macsWithErros = [];
    this.uploadService.setMacsWithErros(this.macsWithErros!);
  }

  public createBoardsToCreateForFile(): void {
    const createBoard = this.createModelToCreateForFile();
    this.resetData();
    this.interfaceBoardService.createBoard(createBoard).subscribe({
      next: (res: IRequestStatus) => this.onProccessResponse(res!),
      error: (error: HttpErrorResponse) => this.onError(error.error.message),
    });
  }

  private onProccessResponse(res: IRequestStatus): void {
    if (res.code == 200) {
      return this.onSuccess(res.msg!);
    }
    this.macsWithErros = res.msg!.split(',') as string[];
    this.quantityOfErrors = this.macsWithErros!.length;
    this.onError(res.title!);
  }

  private onSuccess(message: string): void {
    this.showAlert('success', message, 2000, true);
  }

  private onError(message: string): void {
    this.showAlert('danger', message, 4000, false);
  }

  createModelToCreateForFile(): ICreateBoard {
    return {
      ...new CreateBoard(),
      receptionOrder: this.formCreateInterfaceBoard.get(['receptionOrderOption'])!.value as IReceptionOrder,
      macs: this.macs,
    };
  }

  public showAlert(typeAlertErrorMsg: string, errorMsg: string, showTime: number, closeModal: boolean): void {
    this.typeAlertErrorMsg = typeAlertErrorMsg;
    this.errorMsg = errorMsg!;

    setTimeout(() => {
      this.errorMsg = '';
      closeModal ? this.modalService.dismissAll() : null;
    }, showTime);
  }

  onSelectOption() {
    this.receptionOrderChild = this.formCreateInterfaceBoard.get(['receptionOrderOption'])!.value as IReceptionOrder;
  }

  public download(): void {
    this.uploadService.setMacsWithErros(this.macsWithErros!);
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
