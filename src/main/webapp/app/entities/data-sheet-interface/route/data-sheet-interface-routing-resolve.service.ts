import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDataSheetInterface } from '../data-sheet-interface.model';
import { DataSheetInterfaceService } from '../service/data-sheet-interface.service';

@Injectable({ providedIn: 'root' })
export class DataSheetInterfaceRoutingResolveService implements Resolve<IDataSheetInterface | null> {
  constructor(protected service: DataSheetInterfaceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDataSheetInterface | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dataSheetInterface: HttpResponse<IDataSheetInterface>) => {
          if (dataSheetInterface.body) {
            return of(dataSheetInterface.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
