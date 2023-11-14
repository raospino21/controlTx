import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { StoreComponent } from './store-admin.component';
import { LinkBoardComponent } from './component-children/link-board/link-board.component';
import { BoardInStockComponent } from './component-children/board-in-stock/board-in-stock.component';
import { STORE_ROUTE } from './store-admin.route';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([STORE_ROUTE], { useHash: true })],
  declarations: [StoreComponent, LinkBoardComponent, BoardInStockComponent],
  exports: [BoardInStockComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class StoreAdminModule {}
