import { Injectable, NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LinkBoardComponent } from './link-board/link-board.component';

@Injectable({ providedIn: 'root' })
export class StoreChildren {}

export const STORE_LIST_ROUTE: Route = {
  path: 'link-contract-board',
  component: LinkBoardComponent,
  data: {
    authorities: ['ROLE_STORE'],
    defaultSort: 'id,asc',
    pageTitle: 'store-admin.home.title',
  },
  canActivate: [UserRouteAccessService],
};

const ROUTES: Routes = [STORE_LIST_ROUTE];

@NgModule({
  imports: [RouterModule.forChild(ROUTES)],
  exports: [RouterModule],
})
export class StoreChildrenRouting {}
