import { Injectable, NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManagerStoreComponent } from './store/store.component';
import { ManagerGeneralComponent } from './general/general.component';

@Injectable({ providedIn: 'root' })
export class ManagerChildren {}

export const MANAGER_STORE_ROUTE: Route = {
  path: 'store',
  component: ManagerStoreComponent,
  data: {
    authorities: ['ROLE_MANAGER'],
    defaultSort: 'id,asc',
    pageTitle: 'controTxApp.manager.list.store',
  },
  canActivate: [UserRouteAccessService],
};

export const MANAGER_GENERAL_ROUTE: Route = {
  path: 'general',
  component: ManagerGeneralComponent,
  data: {
    authorities: ['ROLE_MANAGER'],
    defaultSort: 'id,asc',
    pageTitle: 'controTxApp.manager.list.general',
  },
  canActivate: [UserRouteAccessService],
};

const ROUTES: Routes = [MANAGER_STORE_ROUTE, MANAGER_GENERAL_ROUTE];

@NgModule({
  imports: [RouterModule.forChild(ROUTES)],
  exports: [RouterModule],
})
export class ManagerChildrenRouting {}
