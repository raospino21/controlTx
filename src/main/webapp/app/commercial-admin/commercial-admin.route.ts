import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommercialComponent } from './commercial-admin.component';
import { COMMERCIAL_LIST_ROUTE } from './component-children/commercial-children-routing.module';

const CHILDREN = [COMMERCIAL_LIST_ROUTE];

export const COMMERCIAL_ROUTE: Route = {
  path: 'commercial-admin',
  component: CommercialComponent,
  data: {
    authorities: ['ROLE_COMMERCIAL'],
    pageTitle: 'commercial.title',
  },
  canActivate: [UserRouteAccessService],
  children: CHILDREN,
};
