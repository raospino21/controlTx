import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InterfaceBoardComponent } from '../list/interface-board.component';
import { InterfaceBoardDetailComponent } from '../detail/interface-board-detail.component';
import { InterfaceBoardUpdateComponent } from '../update/interface-board-update.component';
import { InterfaceBoardRoutingResolveService } from './interface-board-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const interfaceBoardRoute: Routes = [
  {
    path: '',
    component: InterfaceBoardComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InterfaceBoardDetailComponent,
    resolve: {
      interfaceBoard: InterfaceBoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InterfaceBoardUpdateComponent,
    resolve: {
      interfaceBoard: InterfaceBoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InterfaceBoardUpdateComponent,
    resolve: {
      interfaceBoard: InterfaceBoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(interfaceBoardRoute)],
  exports: [RouterModule],
})
export class InterfaceBoardRoutingModule {}
