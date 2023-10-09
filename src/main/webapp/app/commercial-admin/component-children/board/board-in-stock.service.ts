import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IInterfaceBoard } from 'app/entities/interface-board/interface-board.model';
import { Observable } from 'rxjs/internal/Observable';

export type EntityArrayResponseType = HttpResponse<IInterfaceBoard[]>;

@Injectable({ providedIn: 'root' })
export class CommercialService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getBoardsInStock(page: number, pageSize: number): Observable<HttpResponse<IInterfaceBoard[]>> {
    return this.http.get<IInterfaceBoard[]>(`${this.resourceUrl}/info/boards/available?page=${page}&size=${pageSize}`, {
      observe: 'response',
    });
  }
}
