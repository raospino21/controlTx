import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBrand } from 'app/entities/brand/brand.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ManagerGeneralService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getBrands(req?: any): Observable<HttpResponse<IBrand[]>> {
    const options = createRequestOption(req);
    return this.http.get<IBrand[]>(this.resourceUrl + '/brands', { params: options, observe: 'response' });
  }
}
