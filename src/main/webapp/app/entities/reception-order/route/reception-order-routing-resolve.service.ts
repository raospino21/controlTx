import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReceptionOrder } from '../reception-order.model';
import { ReceptionOrderService } from '../service/reception-order.service';

@Injectable({ providedIn: 'root' })
export class ReceptionOrderRoutingResolveService implements Resolve<IReceptionOrder | null> {
  constructor(protected service: ReceptionOrderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReceptionOrder | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((receptionOrder: HttpResponse<IReceptionOrder>) => {
          if (receptionOrder.body) {
            return of(receptionOrder.body);
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
