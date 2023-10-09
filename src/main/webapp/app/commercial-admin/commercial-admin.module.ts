import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';

import { CommercialComponent } from './commercial-admin.component';
import { COMMERCIAL_ROUTE } from './commercial-admin.route';
import { BoardInStockComponent } from './component-children/board/board-in-stock.component';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([COMMERCIAL_ROUTE], { useHash: true })],
  declarations: [CommercialComponent, BoardInStockComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class CommercialAdminModule {}
