import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ControlInterfaceBoardComponent } from '../list/control-interface-board.component';
import { ControlInterfaceBoardDetailComponent } from '../detail/control-interface-board-detail.component';
import { ControlInterfaceBoardUpdateComponent } from '../update/control-interface-board-update.component';
import { ControlInterfaceBoardRoutingResolveService } from './control-interface-board-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const controlInterfaceBoardRoute: Routes = [
  {
    path: '',
    component: ControlInterfaceBoardComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ControlInterfaceBoardDetailComponent,
    resolve: {
      controlInterfaceBoard: ControlInterfaceBoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ControlInterfaceBoardUpdateComponent,
    resolve: {
      controlInterfaceBoard: ControlInterfaceBoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ControlInterfaceBoardUpdateComponent,
    resolve: {
      controlInterfaceBoard: ControlInterfaceBoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(controlInterfaceBoardRoute)],
  exports: [RouterModule],
})
export class ControlInterfaceBoardRoutingModule {}
