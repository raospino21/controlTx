import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';

import { StoreComponent } from './store-admin.component';
import { STORE_ROUTE } from './store-admin.route';
import { ReportStoreComponent } from './component-children/report/report-store.component';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([STORE_ROUTE], { useHash: true })],
  declarations: [StoreComponent, ReportStoreComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class StoreAdminModule {}
