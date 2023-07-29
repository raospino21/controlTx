import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IControlInterfaceBoard, NewControlInterfaceBoard } from '../control-interface-board.model';

export type PartialUpdateControlInterfaceBoard = Partial<IControlInterfaceBoard> & Pick<IControlInterfaceBoard, 'id'>;

type RestOf<T extends IControlInterfaceBoard | NewControlInterfaceBoard> = Omit<T, 'startTime' | 'finishTime'> & {
  startTime?: string | null;
  finishTime?: string | null;
};

export type RestControlInterfaceBoard = RestOf<IControlInterfaceBoard>;

export type NewRestControlInterfaceBoard = RestOf<NewControlInterfaceBoard>;

export type PartialUpdateRestControlInterfaceBoard = RestOf<PartialUpdateControlInterfaceBoard>;

export type EntityResponseType = HttpResponse<IControlInterfaceBoard>;
export type EntityArrayResponseType = HttpResponse<IControlInterfaceBoard[]>;

@Injectable({ providedIn: 'root' })
export class ControlInterfaceBoardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/control-interface-boards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(controlInterfaceBoard: NewControlInterfaceBoard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(controlInterfaceBoard);
    return this.http
      .post<RestControlInterfaceBoard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(controlInterfaceBoard: IControlInterfaceBoard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(controlInterfaceBoard);
    return this.http
      .put<RestControlInterfaceBoard>(`${this.resourceUrl}/${this.getControlInterfaceBoardIdentifier(controlInterfaceBoard)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(controlInterfaceBoard: PartialUpdateControlInterfaceBoard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(controlInterfaceBoard);
    return this.http
      .patch<RestControlInterfaceBoard>(`${this.resourceUrl}/${this.getControlInterfaceBoardIdentifier(controlInterfaceBoard)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestControlInterfaceBoard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestControlInterfaceBoard[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getControlInterfaceBoardIdentifier(controlInterfaceBoard: Pick<IControlInterfaceBoard, 'id'>): number {
    return controlInterfaceBoard.id;
  }

  compareControlInterfaceBoard(o1: Pick<IControlInterfaceBoard, 'id'> | null, o2: Pick<IControlInterfaceBoard, 'id'> | null): boolean {
    return o1 && o2 ? this.getControlInterfaceBoardIdentifier(o1) === this.getControlInterfaceBoardIdentifier(o2) : o1 === o2;
  }

  addControlInterfaceBoardToCollectionIfMissing<Type extends Pick<IControlInterfaceBoard, 'id'>>(
    controlInterfaceBoardCollection: Type[],
    ...controlInterfaceBoardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const controlInterfaceBoards: Type[] = controlInterfaceBoardsToCheck.filter(isPresent);
    if (controlInterfaceBoards.length > 0) {
      const controlInterfaceBoardCollectionIdentifiers = controlInterfaceBoardCollection.map(
        controlInterfaceBoardItem => this.getControlInterfaceBoardIdentifier(controlInterfaceBoardItem)!
      );
      const controlInterfaceBoardsToAdd = controlInterfaceBoards.filter(controlInterfaceBoardItem => {
        const controlInterfaceBoardIdentifier = this.getControlInterfaceBoardIdentifier(controlInterfaceBoardItem);
        if (controlInterfaceBoardCollectionIdentifiers.includes(controlInterfaceBoardIdentifier)) {
          return false;
        }
        controlInterfaceBoardCollectionIdentifiers.push(controlInterfaceBoardIdentifier);
        return true;
      });
      return [...controlInterfaceBoardsToAdd, ...controlInterfaceBoardCollection];
    }
    return controlInterfaceBoardCollection;
  }

  protected convertDateFromClient<T extends IControlInterfaceBoard | NewControlInterfaceBoard | PartialUpdateControlInterfaceBoard>(
    controlInterfaceBoard: T
  ): RestOf<T> {
    return {
      ...controlInterfaceBoard,
      startTime: controlInterfaceBoard.startTime?.toJSON() ?? null,
      finishTime: controlInterfaceBoard.finishTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restControlInterfaceBoard: RestControlInterfaceBoard): IControlInterfaceBoard {
    return {
      ...restControlInterfaceBoard,
      startTime: restControlInterfaceBoard.startTime ? dayjs(restControlInterfaceBoard.startTime) : undefined,
      finishTime: restControlInterfaceBoard.finishTime ? dayjs(restControlInterfaceBoard.finishTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestControlInterfaceBoard>): HttpResponse<IControlInterfaceBoard> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestControlInterfaceBoard[]>): HttpResponse<IControlInterfaceBoard[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
