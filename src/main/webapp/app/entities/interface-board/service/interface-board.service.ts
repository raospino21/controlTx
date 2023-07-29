import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInterfaceBoard, NewInterfaceBoard } from '../interface-board.model';

export type PartialUpdateInterfaceBoard = Partial<IInterfaceBoard> & Pick<IInterfaceBoard, 'id'>;

export type EntityResponseType = HttpResponse<IInterfaceBoard>;
export type EntityArrayResponseType = HttpResponse<IInterfaceBoard[]>;

@Injectable({ providedIn: 'root' })
export class InterfaceBoardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/interface-boards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(interfaceBoard: NewInterfaceBoard): Observable<EntityResponseType> {
    return this.http.post<IInterfaceBoard>(this.resourceUrl, interfaceBoard, { observe: 'response' });
  }

  update(interfaceBoard: IInterfaceBoard): Observable<EntityResponseType> {
    return this.http.put<IInterfaceBoard>(`${this.resourceUrl}/${this.getInterfaceBoardIdentifier(interfaceBoard)}`, interfaceBoard, {
      observe: 'response',
    });
  }

  partialUpdate(interfaceBoard: PartialUpdateInterfaceBoard): Observable<EntityResponseType> {
    return this.http.patch<IInterfaceBoard>(`${this.resourceUrl}/${this.getInterfaceBoardIdentifier(interfaceBoard)}`, interfaceBoard, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInterfaceBoard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterfaceBoard[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInterfaceBoardIdentifier(interfaceBoard: Pick<IInterfaceBoard, 'id'>): number {
    return interfaceBoard.id;
  }

  compareInterfaceBoard(o1: Pick<IInterfaceBoard, 'id'> | null, o2: Pick<IInterfaceBoard, 'id'> | null): boolean {
    return o1 && o2 ? this.getInterfaceBoardIdentifier(o1) === this.getInterfaceBoardIdentifier(o2) : o1 === o2;
  }

  addInterfaceBoardToCollectionIfMissing<Type extends Pick<IInterfaceBoard, 'id'>>(
    interfaceBoardCollection: Type[],
    ...interfaceBoardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const interfaceBoards: Type[] = interfaceBoardsToCheck.filter(isPresent);
    if (interfaceBoards.length > 0) {
      const interfaceBoardCollectionIdentifiers = interfaceBoardCollection.map(
        interfaceBoardItem => this.getInterfaceBoardIdentifier(interfaceBoardItem)!
      );
      const interfaceBoardsToAdd = interfaceBoards.filter(interfaceBoardItem => {
        const interfaceBoardIdentifier = this.getInterfaceBoardIdentifier(interfaceBoardItem);
        if (interfaceBoardCollectionIdentifiers.includes(interfaceBoardIdentifier)) {
          return false;
        }
        interfaceBoardCollectionIdentifiers.push(interfaceBoardIdentifier);
        return true;
      });
      return [...interfaceBoardsToAdd, ...interfaceBoardCollection];
    }
    return interfaceBoardCollection;
  }
}
