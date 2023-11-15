import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StoreComponent } from './store-admin.component';
import { BOARD_ROUTE, STORE_LIST_ROUTE } from './component-children/store-children-routing.module';

const CHILDREN = [BOARD_ROUTE, STORE_LIST_ROUTE];

export const STORE_ROUTE: Route = {
  path: 'store-admin',
  component: StoreComponent,
  data: {
    authorities: ['ROLE_STORE', 'ROLE_COMMERCIAL', 'ROLE_MANAGER'],
    pageTitle: 'store.title',
  },
  canActivate: [UserRouteAccessService],
  children: CHILDREN,
};
