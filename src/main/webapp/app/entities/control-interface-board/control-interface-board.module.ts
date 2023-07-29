import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ControlInterfaceBoardComponent } from './list/control-interface-board.component';
import { ControlInterfaceBoardDetailComponent } from './detail/control-interface-board-detail.component';
import { ControlInterfaceBoardUpdateComponent } from './update/control-interface-board-update.component';
import { ControlInterfaceBoardDeleteDialogComponent } from './delete/control-interface-board-delete-dialog.component';
import { ControlInterfaceBoardRoutingModule } from './route/control-interface-board-routing.module';

@NgModule({
  imports: [SharedModule, ControlInterfaceBoardRoutingModule],
  declarations: [
    ControlInterfaceBoardComponent,
    ControlInterfaceBoardDetailComponent,
    ControlInterfaceBoardUpdateComponent,
    ControlInterfaceBoardDeleteDialogComponent,
  ],
})
export class ControlInterfaceBoardModule {}
