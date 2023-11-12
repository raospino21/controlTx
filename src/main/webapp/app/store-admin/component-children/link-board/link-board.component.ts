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

import { AssignBoard } from './assign-board.model';
import { IRequestStatus } from 'app/shared/request-status.model';
import { StoreService } from '../store.service';
import { IContractSub } from 'app/manager-admin/component-children/general/contratsub.model';

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
  contractSubList: IContractSub[] = [];
  controlInterfaceBoards?: IControlInterfaceBoard[];
  firstTimeStatus = true;
  public errorMsg = '';
  public typeAlertErrorMsg = 'danger';
  pageSize: number = ITEMS_PER_PAGE;
  page = 1;
  totalItems = 0;
  viable2Assign = true;
  numberBoardInStock = 0;
  canView(role: string[]) {
    return this.accountService.hasAnyAuthority(role);
  }

  ngOnInit(): void {
    this.loadData();
    this.formAssignBoard.get('amountToAssociate')?.valueChanges.subscribe(newValue => {
      this.viable2Assign =
        newValue! <= this.infoContracts.boardContracted - this.infoContracts.boardsAssigned && newValue! <= this.numberBoardInStock;
    });
  }

  loadData(): void {
    const queryObject: any = {
      page: this.page - 1,
      size: this.pageSize,
      reference: this.filters.reference,
      mac: this.filters!.mac!.trim(),
    };

    this.service.getControlBoardsLinked(queryObject).subscribe({
      next: (res: HttpResponse<IControlInterfaceBoard[]>) => this.onSuccess(res.body!, res.headers),
      error: (error: HttpErrorResponse) => this.onError(error),
    });
    this.service.getCountBoardsAvailable().subscribe({
      next: (res: HttpResponse<number>) => (this.numberBoardInStock = res.body!),
      error: (error: HttpErrorResponse) => this.onError(error),
    });
  }

  private onSuccess(controlInterfaceBoards: IControlInterfaceBoard[], headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
    this.controlInterfaceBoards = controlInterfaceBoards;
    this.showAlert('success', 'Exito!', 500);
  }

  private onError(error: HttpErrorResponse): void {
    this.filters.reference = null;
    this.filters.mac = '';
    this.showAlert('danger', error.error.detail, 4000);
  }

  public cleanFilters(): void {
    this.filters.reference = null;
    this.filters.mac = '';
    this.loadData();
  }

  public containsFilter(): boolean {
    return this.filters.reference !== null || this.filters.mac !== '';
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
    mac: '',
  };

  public filter(): void {
    this.page = 1;
    this.filters!.mac = this.filters!.mac!.trim();
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
    amountToAssociate: [null, [Validators.required, Validators.min(1)]],
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

  onSelectContractChange(event: any) {
    const selection: string = event.target.value;

    this.formAssignBoard.patchValue({
      contractType: null,
      amountToAssociate: null,
    });
    this.contractsType = this.contracts!.filter(contract => contract.reference === selection).map(
      contract => contract.type
    ) as ContractType[];
    this.service.getAssociatedBoardsByReference(selection).subscribe({
      next: (res: HttpResponse<IContractSub[]>) => (this.contractSubList = res.body!),
      error: (error: HttpErrorResponse) => this.showAlert('danger', error.error.detail, 4000),
    });
  }

  onSelectContractTypeChange(event: any) {
    const selection: string = event.target.value;

    const referenceSelected = this.formAssignBoard.get(['reference'])!.value;

    const contractSelected = this.contracts!.find(
      contract => contract.reference === referenceSelected && contract.type === selection
    ) as IContract;
    this.infoContracts.boardContracted = contractSelected.amountInterfaceBoard!;

    const contractTypeSelected = this.formAssignBoard.get(['contractType'])!.value;

    const contractSubSelected = this.contractSubList!.find(
      info => info.reference === referenceSelected && info.contractType === contractTypeSelected
    ) as IContractSub;

    this.infoContracts.boardsAssigned = contractSubSelected.amountInterfaceBoardAssigned!;
    this.calculateMaxAmountForAssignment();
  }
  calculateMaxAmountForAssignment(): void {
    const amountAvalaibleForContract = this.infoContracts.boardContracted - this.infoContracts.boardsAssigned;
    this.infoContracts.maxAmountForAssignment =
      this.numberBoardInStock > amountAvalaibleForContract ? amountAvalaibleForContract : this.numberBoardInStock;
  }
  assignBoard(): void {
    const assignBoard = {
      ...new AssignBoard(),
      reference: this.formAssignBoard.get(['reference'])!.value,
      contractType: this.formAssignBoard.get(['contractType'])!.value,
      amountToAssociate: this.formAssignBoard.get(['amountToAssociate'])?.value,
    };

    this.service.assignInterfaceBoard(assignBoard).subscribe({
      next: (result: HttpResponse<Blob>) => {
        const bodyResponse = result.body as Blob;

        console.log('res.headers ', result.headers);
        const fileName = result.headers.get('filename') as string;

        const link = document.createElement('a');
        link.href = URL.createObjectURL(bodyResponse);
        link.download = fileName;
        link.click();

        URL.revokeObjectURL(link.href);
        this.showAlert('success', 'Exito!', 500);
        this.loadData();
        this.cleanFormAssignBoard();
      },
      error: (error: HttpErrorResponse) => {
        console.log(error);
        console.log(error.error.detail);
        alert('Error al descargar el archivo');
      },
    });
  }

  public cleanFormAssignBoard(): void {
    this.formAssignBoard.patchValue({
      contractType: null,
      reference: null,
      amountToAssociate: null,
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

  infoContracts = {
    boardContracted: 0,
    boardsAssigned: 0,
    maxAmountForAssignment: 0,
  };
}
