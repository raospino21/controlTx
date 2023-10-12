import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { IControlInterfaceBoard } from 'app/entities/control-interface-board/control-interface-board.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { IContract } from 'app/entities/contract/contract.model';
import { NgbModal, NgbPanelChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { StoreService } from './link-board.service';
import { AssignBoard } from './assign-board.model';
import { IRequestStatus } from 'app/shared/request-status.model';

@Component({
  selector: 'jhi-report',
  templateUrl: './link-board.component.html',
  styleUrls: ['./link-board.component.scss'],
})
export class LinkBoardComponent implements OnInit {
  @ViewChild('assignBoardTpl', { static: true }) assignBoardTpl: TemplateRef<any> | undefined;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    public accountService: AccountService,
    protected service: StoreService,
    protected fb: FormBuilder,
    private modalService: NgbModal
  ) {}

  contracts?: IContract[];
  contractsFilter?: IContract[];
  contractsType: ContractType[] = [];
  controlInterfaceBoards?: IControlInterfaceBoard[];
  firstTimeStatus = true;
  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';
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

  modalAssignBoard(): void {
    this.openModal(this.assignBoardTpl);
  }

  public beforeClose($event: NgbPanelChangeEvent): void {
    if ($event.panelId === 'toggle-1' && $event.nextState === false && !this.firstTimeStatus) {
      $event.preventDefault();
    }
  }

  private openModal(assignBoard: any): void {
    this.modalService.open(assignBoard, {
      backdrop: 'static',
      keyboard: false,
    });
    this.getPendingContractsForBoard();
  }

  formAssignBoard = this.fb.group({
    contractType: [null, [Validators.required]],
    reference: [null, [Validators.required]],
    mac: [null, [Validators.required]],
  });

  getPendingContractsForBoard(): void {
    this.service.getPendingContractsForBoard().subscribe(res => {
      this.contracts = res.body!;
      const valoresVistos = new Set();
      this.contractsFilter = this.contracts!.filter(contract => {
        if (!valoresVistos.has(contract.reference)) {
          valoresVistos.add(contract.reference);
          return true;
        }
        return false;
      });
    });
  }

  onSelectChange(event: any) {
    const selection: string = event.target.value;
    this.contractsType = this.contracts!.filter(contract => contract.reference === selection).map(
      contract => contract.type
    ) as ContractType[];
  }

  assignBoard(): void {
    const assignBoard = {
      ...new AssignBoard(),
      reference: this.formAssignBoard.get(['reference'])!.value,
      contractType: this.formAssignBoard.get(['contractType'])!.value,
      macs: [this.formAssignBoard.get(['mac'])?.value],
    };

    this.service.assignInterfaceBoard(assignBoard).subscribe(
      (res: IRequestStatus) => {
        this.showAlert('success', res.msg!, 4000);
      },
      (error: HttpErrorResponse) => {
        this.showAlert('danger', error.error.detail, 4000);
      }
    );
  }

  public cleanFormAssignBoard(): void {
    this.formAssignBoard.patchValue({
      contractType: null,
      reference: null,
      mac: null,
    });
  }

  public showAlert(typeAlertErrorMsg: string, errorMsg: string, showTime: number): void {
    this.typeAlertErrorMsg = typeAlertErrorMsg;
    this.errorMsg = errorMsg!;

    setTimeout(() => {
      this.errorMsg = '';
      this.modalService.dismissAll();
    }, showTime);
  }
}
