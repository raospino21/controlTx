import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManagerComponent } from './manager-admin.component';
import { MANAGER_GENERAL_ROUTE, MANAGER_STORE_ROUTE } from './component-children/manager-children-routing.module';

const CHILDREN = [MANAGER_STORE_ROUTE, MANAGER_GENERAL_ROUTE];

export const MANAGER_ROUTE: Route = {
  path: 'manager-admin',
  component: ManagerComponent,
  data: {
    authorities: ['ROLE_MANAGER'],
    pageTitle: 'global.menu.manager.title',
  },
  canActivate: [UserRouteAccessService],
  children: CHILDREN,
};
