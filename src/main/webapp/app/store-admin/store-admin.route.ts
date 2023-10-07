import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StoreComponent } from './store-admin.component';
import { STORE_LIST_ROUTE } from './component-children/store-children-routing.module';

const CHILDREN = [STORE_LIST_ROUTE];

export const STORE_ROUTE: Route = {
  path: 'store-admin',
  component: StoreComponent,
  data: {
    authorities: ['ROLE_STORE'],
    pageTitle: 'store.title',
  },
  canActivate: [UserRouteAccessService],
  children: CHILDREN,
};
