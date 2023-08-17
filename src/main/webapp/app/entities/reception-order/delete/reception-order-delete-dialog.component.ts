import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReceptionOrder } from '../reception-order.model';
import { ReceptionOrderService } from '../service/reception-order.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './reception-order-delete-dialog.component.html',
})
export class ReceptionOrderDeleteDialogComponent {
  receptionOrder?: IReceptionOrder;

  constructor(protected receptionOrderService: ReceptionOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.receptionOrderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
