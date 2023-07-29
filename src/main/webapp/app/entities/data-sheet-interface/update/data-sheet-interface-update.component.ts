import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataSheetInterfaceFormService, DataSheetInterfaceFormGroup } from './data-sheet-interface-form.service';
import { IDataSheetInterface } from '../data-sheet-interface.model';
import { DataSheetInterfaceService } from '../service/data-sheet-interface.service';

@Component({
  selector: 'jhi-data-sheet-interface-update',
  templateUrl: './data-sheet-interface-update.component.html',
})
export class DataSheetInterfaceUpdateComponent implements OnInit {
  isSaving = false;
  dataSheetInterface: IDataSheetInterface | null = null;

  editForm: DataSheetInterfaceFormGroup = this.dataSheetInterfaceFormService.createDataSheetInterfaceFormGroup();

  constructor(
    protected dataSheetInterfaceService: DataSheetInterfaceService,
    protected dataSheetInterfaceFormService: DataSheetInterfaceFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataSheetInterface }) => {
      this.dataSheetInterface = dataSheetInterface;
      if (dataSheetInterface) {
        this.updateForm(dataSheetInterface);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dataSheetInterface = this.dataSheetInterfaceFormService.getDataSheetInterface(this.editForm);
    if (dataSheetInterface.id !== null) {
      this.subscribeToSaveResponse(this.dataSheetInterfaceService.update(dataSheetInterface));
    } else {
      this.subscribeToSaveResponse(this.dataSheetInterfaceService.create(dataSheetInterface));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataSheetInterface>>): void {
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

  protected updateForm(dataSheetInterface: IDataSheetInterface): void {
    this.dataSheetInterface = dataSheetInterface;
    this.dataSheetInterfaceFormService.resetForm(this.editForm, dataSheetInterface);
  }
}
