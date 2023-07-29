import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InterfaceBoardComponent } from './list/interface-board.component';
import { InterfaceBoardDetailComponent } from './detail/interface-board-detail.component';
import { InterfaceBoardUpdateComponent } from './update/interface-board-update.component';
import { InterfaceBoardDeleteDialogComponent } from './delete/interface-board-delete-dialog.component';
import { InterfaceBoardRoutingModule } from './route/interface-board-routing.module';

@NgModule({
  imports: [SharedModule, InterfaceBoardRoutingModule],
  declarations: [
    InterfaceBoardComponent,
    InterfaceBoardDetailComponent,
    InterfaceBoardUpdateComponent,
    InterfaceBoardDeleteDialogComponent,
  ],
})
export class InterfaceBoardModule {}
