import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { Observable } from 'rxjs/internal/Observable';
import { createRequestOption } from 'app/core/request/request-util';

@Injectable({ providedIn: 'root' })
export class StoreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getControlBoardsAvailable(header?: any, request?: any): Observable<HttpResponse<IInterfaceBoard[]>> {
    const options = createRequestOption(header);
    return this.http.get<IInterfaceBoard[]>(this.resourceUrl + '/info/control-interface-boards/avalaible/', {
      params: options,
      observe: 'response',
    });
  }
}
