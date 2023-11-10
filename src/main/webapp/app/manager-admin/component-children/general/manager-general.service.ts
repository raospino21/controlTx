import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBrand } from 'app/entities/brand/brand.model';
import { Observable } from 'rxjs';
import { IBrandCompleteInfo } from './brand-complete-info.model';
import { IOperatorCompleteInfo } from './operator-complete-info.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { IInterfaceBoard } from '../../../entities/interface-board/interface-board.model';

@Injectable({ providedIn: 'root' })
export class ManagerGeneralService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getBrands(req?: any): Observable<HttpResponse<IBrandCompleteInfo[]>> {
    const options = createRequestOption(req);
    return this.http.get<IBrandCompleteInfo[]>(this.resourceUrl + '/brand/complete/info', { params: options, observe: 'response' });
  }

  getOperators(req?: any): Observable<HttpResponse<IOperatorCompleteInfo[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOperatorCompleteInfo[]>(this.resourceUrl + '/operator/complete/info', { params: options, observe: 'response' });
  }

  getMacByContracTypeReference(reference?: string, type?: ContractType): Observable<HttpResponse<IInterfaceBoard[]>> {
    return this.http.get<IInterfaceBoard[]>(`${this.resourceUrl}/interface-boards/assigned-by-operator/${reference}/${type}`, {
      observe: 'response',
    });
  }

  donwloadOperatorBoards(contractId?: number): Observable<HttpResponse<Blob>> {
    return this.http.get(`${this.resourceUrl}/download/file/operator-boards/${contractId}`, { observe: 'response', responseType: 'blob' });
  }
}
