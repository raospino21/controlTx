import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContractFormService, ContractFormGroup } from './contract-form.service';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { IOperator } from 'app/entities/operator/operator.model';
import { OperatorService } from 'app/entities/operator/service/operator.service';
import { ContractType } from 'app/entities/enumerations/contract-type.model';

@Component({
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.component.html',
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;
  isUpdate = false;
  contract: IContract | null = null;
  contractTypeValues = Object.keys(ContractType);

  operatorsSharedCollection: IOperator[] = [];

  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

  constructor(
    protected contractService: ContractService,
    protected contractFormService: ContractFormService,
    protected operatorService: OperatorService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOperator = (o1: IOperator | null, o2: IOperator | null): boolean => this.operatorService.compareOperator(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.contract = contract;
      if (contract) {
        this.updateForm(contract);
        this.isUpdate = true;
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.contractFormService.getContract(this.editForm);
    if (contract.id !== null) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
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

  protected updateForm(contract: IContract): void {
    this.contract = contract;
    this.contractFormService.resetForm(this.editForm, contract);

    this.operatorsSharedCollection = this.operatorService.addOperatorToCollectionIfMissing<IOperator>(
      this.operatorsSharedCollection,
      contract.operator
    );
  }

  protected loadRelationshipsOptions(): void {
    const queryObject: any = {
      page: 0,
      size: 100,
      eagerload: true,
    };

    this.operatorService
      .query(queryObject)
      .pipe(map((res: HttpResponse<IOperator[]>) => res.body ?? []))
      .pipe(
        map((operators: IOperator[]) =>
          this.operatorService.addOperatorToCollectionIfMissing<IOperator>(operators, this.contract?.operator)
        )
      )
      .subscribe((operators: IOperator[]) => (this.operatorsSharedCollection = operators));
  }
}
