import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDataSheetInterface, NewDataSheetInterface } from '../data-sheet-interface.model';

export type PartialUpdateDataSheetInterface = Partial<IDataSheetInterface> & Pick<IDataSheetInterface, 'id'>;

type RestOf<T extends IDataSheetInterface | NewDataSheetInterface> = Omit<T, 'entryDate'> & {
  entryDate?: string | null;
};

export type RestDataSheetInterface = RestOf<IDataSheetInterface>;

export type NewRestDataSheetInterface = RestOf<NewDataSheetInterface>;

export type PartialUpdateRestDataSheetInterface = RestOf<PartialUpdateDataSheetInterface>;

export type EntityResponseType = HttpResponse<IDataSheetInterface>;
export type EntityArrayResponseType = HttpResponse<IDataSheetInterface[]>;

@Injectable({ providedIn: 'root' })
export class DataSheetInterfaceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/data-sheet-interfaces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dataSheetInterface: NewDataSheetInterface): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataSheetInterface);
    return this.http
      .post<RestDataSheetInterface>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(dataSheetInterface: IDataSheetInterface): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataSheetInterface);
    return this.http
      .put<RestDataSheetInterface>(`${this.resourceUrl}/${this.getDataSheetInterfaceIdentifier(dataSheetInterface)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(dataSheetInterface: PartialUpdateDataSheetInterface): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataSheetInterface);
    return this.http
      .patch<RestDataSheetInterface>(`${this.resourceUrl}/${this.getDataSheetInterfaceIdentifier(dataSheetInterface)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDataSheetInterface>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDataSheetInterface[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDataSheetInterfaceIdentifier(dataSheetInterface: Pick<IDataSheetInterface, 'id'>): number {
    return dataSheetInterface.id;
  }

  compareDataSheetInterface(o1: Pick<IDataSheetInterface, 'id'> | null, o2: Pick<IDataSheetInterface, 'id'> | null): boolean {
    return o1 && o2 ? this.getDataSheetInterfaceIdentifier(o1) === this.getDataSheetInterfaceIdentifier(o2) : o1 === o2;
  }

  addDataSheetInterfaceToCollectionIfMissing<Type extends Pick<IDataSheetInterface, 'id'>>(
    dataSheetInterfaceCollection: Type[],
    ...dataSheetInterfacesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dataSheetInterfaces: Type[] = dataSheetInterfacesToCheck.filter(isPresent);
    if (dataSheetInterfaces.length > 0) {
      const dataSheetInterfaceCollectionIdentifiers = dataSheetInterfaceCollection.map(
        dataSheetInterfaceItem => this.getDataSheetInterfaceIdentifier(dataSheetInterfaceItem)!
      );
      const dataSheetInterfacesToAdd = dataSheetInterfaces.filter(dataSheetInterfaceItem => {
        const dataSheetInterfaceIdentifier = this.getDataSheetInterfaceIdentifier(dataSheetInterfaceItem);
        if (dataSheetInterfaceCollectionIdentifiers.includes(dataSheetInterfaceIdentifier)) {
          return false;
        }
        dataSheetInterfaceCollectionIdentifiers.push(dataSheetInterfaceIdentifier);
        return true;
      });
      return [...dataSheetInterfacesToAdd, ...dataSheetInterfaceCollection];
    }
    return dataSheetInterfaceCollection;
  }

  protected convertDateFromClient<T extends IDataSheetInterface | NewDataSheetInterface | PartialUpdateDataSheetInterface>(
    dataSheetInterface: T
  ): RestOf<T> {
    return {
      ...dataSheetInterface,
      entryDate: dataSheetInterface.entryDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDataSheetInterface: RestDataSheetInterface): IDataSheetInterface {
    return {
      ...restDataSheetInterface,
      entryDate: restDataSheetInterface.entryDate ? dayjs(restDataSheetInterface.entryDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDataSheetInterface>): HttpResponse<IDataSheetInterface> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDataSheetInterface[]>): HttpResponse<IDataSheetInterface[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
