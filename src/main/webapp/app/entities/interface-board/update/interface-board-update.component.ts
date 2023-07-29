import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { InterfaceBoardFormService, InterfaceBoardFormGroup } from './interface-board-form.service';
import { IInterfaceBoard } from '../interface-board.model';
import { InterfaceBoardService } from '../service/interface-board.service';
import { IDataSheetInterface } from 'app/entities/data-sheet-interface/data-sheet-interface.model';
import { DataSheetInterfaceService } from 'app/entities/data-sheet-interface/service/data-sheet-interface.service';

@Component({
  selector: 'jhi-interface-board-update',
  templateUrl: './interface-board-update.component.html',
})
export class InterfaceBoardUpdateComponent implements OnInit {
  isSaving = false;
  interfaceBoard: IInterfaceBoard | null = null;

  dataSheetInterfacesSharedCollection: IDataSheetInterface[] = [];

  editForm: InterfaceBoardFormGroup = this.interfaceBoardFormService.createInterfaceBoardFormGroup();

  constructor(
    protected interfaceBoardService: InterfaceBoardService,
    protected interfaceBoardFormService: InterfaceBoardFormService,
    protected dataSheetInterfaceService: DataSheetInterfaceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDataSheetInterface = (o1: IDataSheetInterface | null, o2: IDataSheetInterface | null): boolean =>
    this.dataSheetInterfaceService.compareDataSheetInterface(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interfaceBoard }) => {
      this.interfaceBoard = interfaceBoard;
      if (interfaceBoard) {
        this.updateForm(interfaceBoard);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interfaceBoard = this.interfaceBoardFormService.getInterfaceBoard(this.editForm);
    if (interfaceBoard.id !== null) {
      this.subscribeToSaveResponse(this.interfaceBoardService.update(interfaceBoard));
    } else {
      this.subscribeToSaveResponse(this.interfaceBoardService.create(interfaceBoard));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterfaceBoard>>): void {
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

  protected updateForm(interfaceBoard: IInterfaceBoard): void {
    this.interfaceBoard = interfaceBoard;
    this.interfaceBoardFormService.resetForm(this.editForm, interfaceBoard);

    this.dataSheetInterfacesSharedCollection =
      this.dataSheetInterfaceService.addDataSheetInterfaceToCollectionIfMissing<IDataSheetInterface>(
        this.dataSheetInterfacesSharedCollection,
        interfaceBoard.dataSheetInterface
      );
  }

  protected loadRelationshipsOptions(): void {
    this.dataSheetInterfaceService
      .query()
      .pipe(map((res: HttpResponse<IDataSheetInterface[]>) => res.body ?? []))
      .pipe(
        map((dataSheetInterfaces: IDataSheetInterface[]) =>
          this.dataSheetInterfaceService.addDataSheetInterfaceToCollectionIfMissing<IDataSheetInterface>(
            dataSheetInterfaces,
            this.interfaceBoard?.dataSheetInterface
          )
        )
      )
      .subscribe((dataSheetInterfaces: IDataSheetInterface[]) => (this.dataSheetInterfacesSharedCollection = dataSheetInterfaces));
  }
}
