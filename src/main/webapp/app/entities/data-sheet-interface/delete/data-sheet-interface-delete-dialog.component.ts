import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataSheetInterface } from '../data-sheet-interface.model';
import { DataSheetInterfaceService } from '../service/data-sheet-interface.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './data-sheet-interface-delete-dialog.component.html',
})
export class DataSheetInterfaceDeleteDialogComponent {
  dataSheetInterface?: IDataSheetInterface;

  constructor(protected dataSheetInterfaceService: DataSheetInterfaceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dataSheetInterfaceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
