import { Injectable, NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LinkBoardComponent } from './link-board/link-board.component';
import { BoardInStockComponent } from './board-in-stock/board-in-stock.component';

@Injectable({ providedIn: 'root' })
export class StoreChildren {}

export const STORE_LIST_ROUTE: Route = {
  path: 'link-contract-board',
  component: LinkBoardComponent,
  data: {
    authorities: ['ROLE_STORE', 'ROLE_COMMERCIAL'],
    defaultSort: 'id,asc',
    pageTitle: 'global.menu.store-admin.link-contract-board.title',
  },
  canActivate: [UserRouteAccessService],
};

export const BOARD_ROUTE: Route = {
  path: 'board-in-stock',
  component: BoardInStockComponent,
  data: {
    authorities: ['ROLE_STORE', 'ROLE_COMMERCIAL', 'ROLE_MANAGER'],
    defaultSort: 'id,asc',
    pageTitle: 'global.menu.store-admin.board-in-stock.title',
  },
  canActivate: [UserRouteAccessService],
};

const ROUTES: Routes = [BOARD_ROUTE, STORE_LIST_ROUTE];

@NgModule({
  imports: [RouterModule.forChild(ROUTES)],
  exports: [RouterModule],
})
export class StoreChildrenRouting {}
