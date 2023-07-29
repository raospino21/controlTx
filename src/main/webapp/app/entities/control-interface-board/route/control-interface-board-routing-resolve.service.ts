import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IControlInterfaceBoard } from '../control-interface-board.model';
import { ControlInterfaceBoardService } from '../service/control-interface-board.service';

@Injectable({ providedIn: 'root' })
export class ControlInterfaceBoardRoutingResolveService implements Resolve<IControlInterfaceBoard | null> {
  constructor(protected service: ControlInterfaceBoardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IControlInterfaceBoard | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((controlInterfaceBoard: HttpResponse<IControlInterfaceBoard>) => {
          if (controlInterfaceBoard.body) {
            return of(controlInterfaceBoard.body);
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
