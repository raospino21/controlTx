import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';

import { ManagerComponent } from './manager-admin.component';
import { MANAGER_ROUTE } from './manager-admin.route';
import { ManagerStoreComponent } from './component-children/store/store.component';
import { PurchaseOrderModule } from 'app/entities/purchase-order/purchase-order.module';

@NgModule({
  imports: [SharedModule, PurchaseOrderModule, RouterModule.forRoot([MANAGER_ROUTE], { useHash: true })],
  declarations: [ManagerComponent, ManagerStoreComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ManagerAdminModule {}
