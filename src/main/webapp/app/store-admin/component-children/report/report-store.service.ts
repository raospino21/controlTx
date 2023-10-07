import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class StoreService {
  protected resourceUrlApiStore = this.applicationConfigService.getEndpointFor('api/store');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}
}
