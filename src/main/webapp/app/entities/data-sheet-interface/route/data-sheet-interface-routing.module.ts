import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DataSheetInterfaceComponent } from '../list/data-sheet-interface.component';
import { DataSheetInterfaceDetailComponent } from '../detail/data-sheet-interface-detail.component';
import { DataSheetInterfaceUpdateComponent } from '../update/data-sheet-interface-update.component';
import { DataSheetInterfaceRoutingResolveService } from './data-sheet-interface-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dataSheetInterfaceRoute: Routes = [
  {
    path: '',
    component: DataSheetInterfaceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DataSheetInterfaceDetailComponent,
    resolve: {
      dataSheetInterface: DataSheetInterfaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DataSheetInterfaceUpdateComponent,
    resolve: {
      dataSheetInterface: DataSheetInterfaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DataSheetInterfaceUpdateComponent,
    resolve: {
      dataSheetInterface: DataSheetInterfaceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dataSheetInterfaceRoute)],
  exports: [RouterModule],
})
export class DataSheetInterfaceRoutingModule {}
