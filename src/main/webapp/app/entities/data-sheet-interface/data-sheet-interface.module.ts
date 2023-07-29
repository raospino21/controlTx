import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DataSheetInterfaceComponent } from './list/data-sheet-interface.component';
import { DataSheetInterfaceDetailComponent } from './detail/data-sheet-interface-detail.component';
import { DataSheetInterfaceUpdateComponent } from './update/data-sheet-interface-update.component';
import { DataSheetInterfaceDeleteDialogComponent } from './delete/data-sheet-interface-delete-dialog.component';
import { DataSheetInterfaceRoutingModule } from './route/data-sheet-interface-routing.module';

@NgModule({
  imports: [SharedModule, DataSheetInterfaceRoutingModule],
  declarations: [
    DataSheetInterfaceComponent,
    DataSheetInterfaceDetailComponent,
    DataSheetInterfaceUpdateComponent,
    DataSheetInterfaceDeleteDialogComponent,
  ],
})
export class DataSheetInterfaceModule {}
