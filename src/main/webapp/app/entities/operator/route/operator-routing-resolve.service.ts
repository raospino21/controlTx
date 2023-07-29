import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperator } from '../operator.model';
import { OperatorService } from '../service/operator.service';

@Injectable({ providedIn: 'root' })
export class OperatorRoutingResolveService implements Resolve<IOperator | null> {
  constructor(protected service: OperatorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperator | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((operator: HttpResponse<IOperator>) => {
          if (operator.body) {
            return of(operator.body);
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
