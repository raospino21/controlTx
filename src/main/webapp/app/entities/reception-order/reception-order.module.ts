import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReceptionOrderComponent } from './list/reception-order.component';
import { ReceptionOrderDetailComponent } from './detail/reception-order-detail.component';
import { ReceptionOrderUpdateComponent } from './update/reception-order-update.component';
import { ReceptionOrderDeleteDialogComponent } from './delete/reception-order-delete-dialog.component';
import { ReceptionOrderRoutingModule } from './route/reception-order-routing.module';

@NgModule({
  imports: [SharedModule, ReceptionOrderRoutingModule],
  declarations: [
    ReceptionOrderComponent,
    ReceptionOrderDetailComponent,
    ReceptionOrderUpdateComponent,
    ReceptionOrderDeleteDialogComponent,
  ],
})
export class ReceptionOrderModule {}
