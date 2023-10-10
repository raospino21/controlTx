import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';

import { StoreComponent } from './store-admin.component';
import { STORE_ROUTE } from './store-admin.route';
import { LinkBoardComponent } from './component-children/link-board/link-board.component';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([STORE_ROUTE], { useHash: true })],
  declarations: [StoreComponent, LinkBoardComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class StoreAdminModule {}
