import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class CommercialService {
  protected resourceUrlApiCommercial = this.applicationConfigService.getEndpointFor('api/commercial');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}
}
