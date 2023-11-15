import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';

import { ManagerComponent } from './manager-admin.component';
import { MANAGER_ROUTE } from './manager-admin.route';
import { ManagerStoreComponent } from './component-children/store/store.component';
import { PurchaseOrderModule } from 'app/entities/purchase-order/purchase-order.module';
import { ManagerGeneralComponent } from './component-children/general/general.component';
import { StoreAdminModule } from 'app/store-admin/store-admin.module';

@NgModule({
  imports: [SharedModule, PurchaseOrderModule, StoreAdminModule, RouterModule.forRoot([MANAGER_ROUTE], { useHash: true })],
  declarations: [ManagerComponent, ManagerStoreComponent, ManagerGeneralComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ManagerAdminModule {}
