import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OperatorFormService, OperatorFormGroup } from './operator-form.service';
import { IOperator } from '../operator.model';
import { OperatorService } from '../service/operator.service';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';

@Component({
  selector: 'jhi-operator-update',
  templateUrl: './operator-update.component.html',
})
export class OperatorUpdateComponent implements OnInit {
  isSaving = false;
  operator: IOperator | null = null;

  brandsSharedCollection: IBrand[] = [];

  editForm: OperatorFormGroup = this.operatorFormService.createOperatorFormGroup();

  constructor(
    protected operatorService: OperatorService,
    protected operatorFormService: OperatorFormService,
    protected brandService: BrandService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBrand = (o1: IBrand | null, o2: IBrand | null): boolean => this.brandService.compareBrand(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operator }) => {
      this.operator = operator;
      if (operator) {
        this.updateForm(operator);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operator = this.operatorFormService.getOperator(this.editForm);
    if (operator.id !== null) {
      this.subscribeToSaveResponse(this.operatorService.update(operator));
    } else {
      this.subscribeToSaveResponse(this.operatorService.create(operator));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperator>>): void {
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

  protected updateForm(operator: IOperator): void {
    this.operator = operator;
    this.operatorFormService.resetForm(this.editForm, operator);

    this.brandsSharedCollection = this.brandService.addBrandToCollectionIfMissing<IBrand>(this.brandsSharedCollection, operator.brand);
  }

  protected loadRelationshipsOptions(): void {
    this.brandService
      .query()
      .pipe(map((res: HttpResponse<IBrand[]>) => res.body ?? []))
      .pipe(map((brands: IBrand[]) => this.brandService.addBrandToCollectionIfMissing<IBrand>(brands, this.operator?.brand)))
      .subscribe((brands: IBrand[]) => (this.brandsSharedCollection = brands));
  }
}
