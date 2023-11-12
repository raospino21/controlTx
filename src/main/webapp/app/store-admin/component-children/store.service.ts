import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { Observable } from 'rxjs/internal/Observable';
import { createRequestOption } from 'app/core/request/request-util';
import { IContract } from 'app/entities/contract/contract.model';

import { IRequestStatus } from 'app/shared/request-status.model';
import { IAssignBoard } from './link-board/assign-board.model';
import { IControlInterfaceBoard } from 'app/entities/control-interface-board/control-interface-board.model';
import { IContractSub } from 'app/manager-admin/component-children/general/contratsub.model';

@Injectable({ providedIn: 'root' })
export class StoreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getControlBoardsLinked(header?: any, request?: any): Observable<HttpResponse<IControlInterfaceBoard[]>> {
    const options = createRequestOption(header);
    return this.http.get<IControlInterfaceBoard[]>(this.resourceUrl + '/info/control-interface-boards/linked/', {
      params: options,
      observe: 'response',
    });
  }

  getCountBoardsAvailable(): Observable<HttpResponse<number>> {
    return this.http.get<number>(this.resourceUrl + '/count/boards/available', {
      observe: 'response',
    });
  }

  getPendingContractsForBoard(): Observable<HttpResponse<IContract[]>> {
    return this.http.get<IContract[]>(this.resourceUrl + '/info/pending/contracts/boards', {
      observe: 'response',
    });
  }

  assignInterfaceBoard(request: IAssignBoard): Observable<IRequestStatus> {
    return this.http.post<IRequestStatus>(this.resourceUrl + '/assign/board', request);
  }

  getBoardsInStock(header?: any): Observable<HttpResponse<IInterfaceBoard[]>> {
    const options = createRequestOption(header);
    return this.http.get<IInterfaceBoard[]>(this.resourceUrl + '/info/boards/available/', {
      params: options,
      observe: 'response',
    });
  }

  getAssociatedBoardsByReference(reference?: string): Observable<HttpResponse<IContractSub[]>> {
    return this.http.get<IContractSub[]>(`${this.resourceUrl}/info/boards/association-by-referece/${reference}/`, {
      observe: 'response',
    });
  }
}
