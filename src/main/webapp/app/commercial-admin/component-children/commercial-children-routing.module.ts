import { Injectable, NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BoardInStockComponent } from './board/board-in-stock.component';

@Injectable({ providedIn: 'root' })
export class CommercialChildren {}

export const COMMERCIAL_LIST_ROUTE: Route = {
  path: 'board-in-stock',
  component: BoardInStockComponent,
  data: {
    authorities: ['ROLE_COMMERCIAL'],
    defaultSort: 'id,asc',
    pageTitle: 'commercial-admin.home.title',
  },
  canActivate: [UserRouteAccessService],
};

const ROUTES: Routes = [COMMERCIAL_LIST_ROUTE];

@NgModule({
  imports: [RouterModule.forChild(ROUTES)],
  exports: [RouterModule],
})
export class CommercialChildrenRouting {}
