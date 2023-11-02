import { Injectable, NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManagerStoreComponent } from './store/store.component';

@Injectable({ providedIn: 'root' })
export class ManagerChildren {}

export const STORE_ROUTE: Route = {
  path: 'store',
  component: ManagerStoreComponent,
  data: {
    authorities: ['ROLE_MANAGER'],
    defaultSort: 'id,asc',
    pageTitle: 'global.menu.manager.store',
  },
  canActivate: [UserRouteAccessService],
};

const ROUTES: Routes = [STORE_ROUTE];

@NgModule({
  imports: [RouterModule.forChild(ROUTES)],
  exports: [RouterModule],
})
export class ManagerChildrenRouting {}
