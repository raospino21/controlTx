import { Injectable, NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

@Injectable({ providedIn: 'root' })
export class CommercialChildren {}

export const COMMERCIAL_LIST_ROUTE: Route = {};

const ROUTES: Routes = [COMMERCIAL_LIST_ROUTE];

@NgModule({
  imports: [RouterModule.forChild(ROUTES)],
  exports: [RouterModule],
})
export class CommercialChildrenRouting {}
