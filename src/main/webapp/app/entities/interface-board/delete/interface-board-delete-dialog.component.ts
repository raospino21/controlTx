import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterfaceBoard } from '../interface-board.model';
import { InterfaceBoardService } from '../service/interface-board.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './interface-board-delete-dialog.component.html',
})
export class InterfaceBoardDeleteDialogComponent {
  interfaceBoard?: IInterfaceBoard;

  constructor(protected interfaceBoardService: InterfaceBoardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interfaceBoardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
