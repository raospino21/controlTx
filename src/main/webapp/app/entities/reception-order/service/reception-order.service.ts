import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReceptionOrder, NewReceptionOrder } from '../reception-order.model';

export type PartialUpdateReceptionOrder = Partial<IReceptionOrder> & Pick<IReceptionOrder, 'id'>;

type RestOf<T extends IReceptionOrder | NewReceptionOrder> = Omit<T, 'entryDate' | 'warrantyDate'> & {
  entryDate?: string | null;
  warrantyDate?: string | null;
};

export type RestReceptionOrder = RestOf<IReceptionOrder>;

export type NewRestReceptionOrder = RestOf<NewReceptionOrder>;

export type PartialUpdateRestReceptionOrder = RestOf<PartialUpdateReceptionOrder>;

export type EntityResponseType = HttpResponse<IReceptionOrder>;
export type EntityArrayResponseType = HttpResponse<IReceptionOrder[]>;

@Injectable({ providedIn: 'root' })
export class ReceptionOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reception-orders');
  protected urlLoadRelationshipsOptions = this.applicationConfigService.getEndpointFor('api/info/reception-order');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(receptionOrder: NewReceptionOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receptionOrder);
    return this.http
      .post<RestReceptionOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(receptionOrder: IReceptionOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receptionOrder);
    return this.http
      .put<RestReceptionOrder>(`${this.resourceUrl}/${this.getReceptionOrderIdentifier(receptionOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(receptionOrder: PartialUpdateReceptionOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receptionOrder);
    return this.http
      .patch<RestReceptionOrder>(`${this.resourceUrl}/${this.getReceptionOrderIdentifier(receptionOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReceptionOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReceptionOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  receptionOrderAvailable(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReceptionOrder[]>(this.urlLoadRelationshipsOptions, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReceptionOrderIdentifier(receptionOrder: Pick<IReceptionOrder, 'id'>): number {
    return receptionOrder.id;
  }

  compareReceptionOrder(o1: Pick<IReceptionOrder, 'id'> | null, o2: Pick<IReceptionOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getReceptionOrderIdentifier(o1) === this.getReceptionOrderIdentifier(o2) : o1 === o2;
  }

  addReceptionOrderToCollectionIfMissing<Type extends Pick<IReceptionOrder, 'id'>>(
    receptionOrderCollection: Type[],
    ...receptionOrdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const receptionOrders: Type[] = receptionOrdersToCheck.filter(isPresent);
    if (receptionOrders.length > 0) {
      const receptionOrderCollectionIdentifiers = receptionOrderCollection.map(
        receptionOrderItem => this.getReceptionOrderIdentifier(receptionOrderItem)!
      );
      const receptionOrdersToAdd = receptionOrders.filter(receptionOrderItem => {
        const receptionOrderIdentifier = this.getReceptionOrderIdentifier(receptionOrderItem);
        if (receptionOrderCollectionIdentifiers.includes(receptionOrderIdentifier)) {
          return false;
        }
        receptionOrderCollectionIdentifiers.push(receptionOrderIdentifier);
        return true;
      });
      return [...receptionOrdersToAdd, ...receptionOrderCollection];
    }
    return receptionOrderCollection;
  }

  protected convertDateFromClient<T extends IReceptionOrder | NewReceptionOrder | PartialUpdateReceptionOrder>(
    receptionOrder: T
  ): RestOf<T> {
    return {
      ...receptionOrder,
      entryDate: receptionOrder.entryDate?.toJSON() ?? null,
      warrantyDate: receptionOrder.warrantyDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReceptionOrder: RestReceptionOrder): IReceptionOrder {
    return {
      ...restReceptionOrder,
      entryDate: restReceptionOrder.entryDate ? dayjs(restReceptionOrder.entryDate) : undefined,
      warrantyDate: restReceptionOrder.warrantyDate ? dayjs(restReceptionOrder.warrantyDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReceptionOrder>): HttpResponse<IReceptionOrder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReceptionOrder[]>): HttpResponse<IReceptionOrder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
