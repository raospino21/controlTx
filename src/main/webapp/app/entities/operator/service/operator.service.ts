import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperator, NewOperator } from '../operator.model';

export type PartialUpdateOperator = Partial<IOperator> & Pick<IOperator, 'id'>;

export type EntityResponseType = HttpResponse<IOperator>;
export type EntityArrayResponseType = HttpResponse<IOperator[]>;

@Injectable({ providedIn: 'root' })
export class OperatorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operators');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operator: NewOperator): Observable<EntityResponseType> {
    return this.http.post<IOperator>(this.resourceUrl, operator, { observe: 'response' });
  }

  update(operator: IOperator): Observable<EntityResponseType> {
    return this.http.put<IOperator>(`${this.resourceUrl}/${this.getOperatorIdentifier(operator)}`, operator, { observe: 'response' });
  }

  partialUpdate(operator: PartialUpdateOperator): Observable<EntityResponseType> {
    return this.http.patch<IOperator>(`${this.resourceUrl}/${this.getOperatorIdentifier(operator)}`, operator, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOperator>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOperator[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOperatorIdentifier(operator: Pick<IOperator, 'id'>): number {
    return operator.id;
  }

  compareOperator(o1: Pick<IOperator, 'id'> | null, o2: Pick<IOperator, 'id'> | null): boolean {
    return o1 && o2 ? this.getOperatorIdentifier(o1) === this.getOperatorIdentifier(o2) : o1 === o2;
  }

  addOperatorToCollectionIfMissing<Type extends Pick<IOperator, 'id'>>(
    operatorCollection: Type[],
    ...operatorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operators: Type[] = operatorsToCheck.filter(isPresent);
    if (operators.length > 0) {
      const operatorCollectionIdentifiers = operatorCollection.map(operatorItem => this.getOperatorIdentifier(operatorItem)!);
      const operatorsToAdd = operators.filter(operatorItem => {
        const operatorIdentifier = this.getOperatorIdentifier(operatorItem);
        if (operatorCollectionIdentifiers.includes(operatorIdentifier)) {
          return false;
        }
        operatorCollectionIdentifiers.push(operatorIdentifier);
        return true;
      });
      return [...operatorsToAdd, ...operatorCollection];
    }
    return operatorCollection;
  }
}
