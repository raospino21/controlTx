import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReceptionOrderComponent } from '../list/reception-order.component';
import { ReceptionOrderDetailComponent } from '../detail/reception-order-detail.component';
import { ReceptionOrderUpdateComponent } from '../update/reception-order-update.component';
import { ReceptionOrderRoutingResolveService } from './reception-order-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const receptionOrderRoute: Routes = [
  {
    path: '',
    component: ReceptionOrderComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReceptionOrderDetailComponent,
    resolve: {
      receptionOrder: ReceptionOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReceptionOrderUpdateComponent,
    resolve: {
      receptionOrder: ReceptionOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReceptionOrderUpdateComponent,
    resolve: {
      receptionOrder: ReceptionOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(receptionOrderRoute)],
  exports: [RouterModule],
})
export class ReceptionOrderRoutingModule {}
