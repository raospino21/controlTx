import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManagerComponent } from './manager-admin.component';
import { STORE_ROUTE } from './component-children/manager-children-routing.module';

const CHILDREN = [STORE_ROUTE];

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
