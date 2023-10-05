import { Injectable, NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportCommercialComponent } from './report/report-commercial.component';

@Injectable({ providedIn: 'root' })
export class CommercialChildren {}

export const COMMERCIAL_LIST_ROUTE: Route = {
  path: 'report',
  component: ReportCommercialComponent,
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
