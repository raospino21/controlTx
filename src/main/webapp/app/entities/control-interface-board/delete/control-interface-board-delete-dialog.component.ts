import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IControlInterfaceBoard } from '../control-interface-board.model';
import { ControlInterfaceBoardService } from '../service/control-interface-board.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './control-interface-board-delete-dialog.component.html',
})
export class ControlInterfaceBoardDeleteDialogComponent {
  controlInterfaceBoard?: IControlInterfaceBoard;

  constructor(protected controlInterfaceBoardService: ControlInterfaceBoardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.controlInterfaceBoardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
