import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInterfaceBoard } from '../interface-board.model';
import { InterfaceBoardService } from '../service/interface-board.service';

@Injectable({ providedIn: 'root' })
export class InterfaceBoardRoutingResolveService implements Resolve<IInterfaceBoard | null> {
  constructor(protected service: InterfaceBoardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterfaceBoard | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((interfaceBoard: HttpResponse<IInterfaceBoard>) => {
          if (interfaceBoard.body) {
            return of(interfaceBoard.body);
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
