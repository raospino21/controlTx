import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ControlInterfaceBoardFormService, ControlInterfaceBoardFormGroup } from './control-interface-board-form.service';
import { IControlInterfaceBoard } from '../control-interface-board.model';
import { ControlInterfaceBoardService } from '../service/control-interface-board.service';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { InterfaceBoardService } from 'app/entities/interface-board/service/interface-board.service';
import { Location } from 'app/entities/enumerations/location.model';
import { StatusInterfaceBoard } from 'app/entities/enumerations/status-interface-board.model';

@Component({
  selector: 'jhi-control-interface-board-update',
  templateUrl: './control-interface-board-update.component.html',
})
export class ControlInterfaceBoardUpdateComponent implements OnInit {
  isSaving = false;
  controlInterfaceBoard: IControlInterfaceBoard | null = null;
  locationValues = Object.keys(Location);
  statusInterfaceBoardValues = Object.keys(StatusInterfaceBoard);

  contractsSharedCollection: IContract[] = [];
  interfaceBoardsSharedCollection: IInterfaceBoard[] = [];

  editForm: ControlInterfaceBoardFormGroup = this.controlInterfaceBoardFormService.createControlInterfaceBoardFormGroup();

  constructor(
    protected controlInterfaceBoardService: ControlInterfaceBoardService,
    protected controlInterfaceBoardFormService: ControlInterfaceBoardFormService,
    protected contractService: ContractService,
    protected interfaceBoardService: InterfaceBoardService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  compareInterfaceBoard = (o1: IInterfaceBoard | null, o2: IInterfaceBoard | null): boolean =>
    this.interfaceBoardService.compareInterfaceBoard(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ controlInterfaceBoard }) => {
      this.controlInterfaceBoard = controlInterfaceBoard;
      if (controlInterfaceBoard) {
        this.updateForm(controlInterfaceBoard);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const controlInterfaceBoard = this.controlInterfaceBoardFormService.getControlInterfaceBoard(this.editForm);
    if (controlInterfaceBoard.id !== null) {
      this.subscribeToSaveResponse(this.controlInterfaceBoardService.update(controlInterfaceBoard));
    } else {
      this.subscribeToSaveResponse(this.controlInterfaceBoardService.create(controlInterfaceBoard));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IControlInterfaceBoard>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(controlInterfaceBoard: IControlInterfaceBoard): void {
    this.controlInterfaceBoard = controlInterfaceBoard;
    this.controlInterfaceBoardFormService.resetForm(this.editForm, controlInterfaceBoard);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      controlInterfaceBoard.contract
    );
    this.interfaceBoardsSharedCollection = this.interfaceBoardService.addInterfaceBoardToCollectionIfMissing<IInterfaceBoard>(
      this.interfaceBoardsSharedCollection,
      controlInterfaceBoard.interfaceBoard
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.controlInterfaceBoard?.contract)
        )
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));

    this.interfaceBoardService
      .query()
      .pipe(map((res: HttpResponse<IInterfaceBoard[]>) => res.body ?? []))
      .pipe(
        map((interfaceBoards: IInterfaceBoard[]) =>
          this.interfaceBoardService.addInterfaceBoardToCollectionIfMissing<IInterfaceBoard>(
            interfaceBoards,
            this.controlInterfaceBoard?.interfaceBoard
          )
        )
      )
      .subscribe((interfaceBoards: IInterfaceBoard[]) => (this.interfaceBoardsSharedCollection = interfaceBoards));
  }
}
